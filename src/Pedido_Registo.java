
import java.io.Serializable;

public class Pedido_Registo implements Serializable {
    
    private String username;
    private String password;
    private String tipo;
    private int aprovado = 0; 
    
    public Pedido_Registo(String user, String pass, String tipo) {
        username = user;
        password = pass;
        this.tipo = tipo;
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
    
}
