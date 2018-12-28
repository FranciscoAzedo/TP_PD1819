
import java.io.Serializable;
import java.util.ArrayList;

public class Pedido_Obter_Ficheiros implements Serializable {
    ArrayList<String> ficheiros;
    String username;
    String ip;
    public Pedido_Obter_Ficheiros(String user){
        username = user;
    }

    public ArrayList<String> getFicheiros() {
        return ficheiros;
    }

    public void setFicheiros(ArrayList<String> ficheiros) {
        this.ficheiros = ficheiros;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
