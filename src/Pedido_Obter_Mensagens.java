
import java.io.Serializable;
import java.util.ArrayList;

public class Pedido_Obter_Mensagens implements Serializable{
        
    private final String user_1;
    private final String user_2;
    private ArrayList<Mensagem> mensagens; 

    public Pedido_Obter_Mensagens(String user_1, String user_2) {
        this.user_1 = user_1;
        this.user_2 = user_2;
        mensagens = new ArrayList<>();
    }

    public String getUser_1() {
        return user_1;
    }

    public String getUser_2() {
        return user_2;
    }

    public void setMensagens(ArrayList<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
    
    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }
}
