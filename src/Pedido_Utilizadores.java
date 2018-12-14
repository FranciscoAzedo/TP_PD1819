
import java.io.Serializable;
import java.util.ArrayList;


public class Pedido_Utilizadores implements Serializable{
    private final String username;
    private ArrayList<String> utilizadores; 

    public Pedido_Utilizadores(String username) {
        this.username = username;
        utilizadores = new ArrayList<>();
    }

    public void setUtilizadores(ArrayList<String> utilizadores) {
        this.utilizadores = utilizadores;
    }

    public ArrayList<String> getUtilizadores() {
        return utilizadores;
    }

    public String getUsername() {
        return username;
    }
}


