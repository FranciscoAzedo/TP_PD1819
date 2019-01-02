
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;

public abstract class Dir_Watcher extends TimerTask {
  private String path;
  private File filesArray [];
  private HashMap<File, Long> dir = new HashMap();
  private Dir_Filter_Watcher dfw;

  public Dir_Watcher(String path){
      this(path, "");
  }
  
  public Dir_Watcher(String path, String filter) {
    this.path = path;
    dfw = new Dir_Filter_Watcher(filter);
    filesArray = new File(path).listFiles(dfw);
    for(int i = 0; i < filesArray.length; i++) {
       dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
    }
  }

  public final void run() {
    HashSet checkedFiles = new HashSet();
    filesArray = new File(path).listFiles(dfw);
    for(int i = 0; i < filesArray.length; i++) {
      Long current = (Long)dir.get(filesArray[i]);
      checkedFiles.add(filesArray[i]);
      if (current == null) {
        dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
        onChange(filesArray[i], "add");
      }
      else if (current.longValue() != filesArray[i].lastModified()){
        dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
        onChange(filesArray[i], "modify");
      }
    }

    Set ref = ((HashMap)dir.clone()).keySet();
    ref.removeAll((Set)checkedFiles);
    Iterator it = ref.iterator();
    while (it.hasNext()) {
      File deletedFile = (File)it.next();
      dir.remove(deletedFile);
      onChange(deletedFile, "delete");
    }
  }

  protected abstract void onChange( File file, String action );
  
}