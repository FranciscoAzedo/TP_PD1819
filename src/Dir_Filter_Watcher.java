
import java.io.File;
import java.io.FileFilter;

public class Dir_Filter_Watcher implements FileFilter {
  private String filter;

  public Dir_Filter_Watcher() {
    this.filter = "";
  }

  public Dir_Filter_Watcher(String filter) {
    this.filter = filter;
  }

  public boolean accept(File file) {
    if ("".equals(filter)) {
      return true;
    }
    return (file.getName().endsWith(filter));
  }
}
