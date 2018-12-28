
import java.io.Serializable;

public class Pedido_Registo implements Serializable {
    
    private final String username;
    private final String password;
    private final String tipo;
    private final String path;
    private int aprovado = 0; 
    
    public Pedido_Registo(String user, String pass, String tipo, String path) {
        username = user;
        password = pass;
        this.tipo = tipo;
        this.path = path;
    }
    
    public void resultado_Pedido(int res){
        aprovado = res;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAprovado() {
        return aprovado;
    }

    public String getTipo() {
        return tipo;
    }

    public String getPath() {
        return path;
    } 
}
