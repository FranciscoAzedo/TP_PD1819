
import java.io.Serializable;


public class Pedido_Alterar_Ficheiro implements Serializable{
    protected String username;
    protected String fileName;
    protected String action;
    protected String tamanho;

    public Pedido_Alterar_Ficheiro(String username, String fileName, String action, String tamanho) {
        this.username = username;
        this.fileName = fileName;
        this.action = action;
        this.tamanho = tamanho;
    }

    public String getUsername() {
        return username;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAction() {
        return action;
    }

    public String getTamanho() {
        return tamanho;
    }
}
