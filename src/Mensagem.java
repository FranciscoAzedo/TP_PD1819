
import java.io.Serializable;

public class Mensagem implements Serializable {
    private final String user_origem;
    private final String user_destino;
    private final String mensagem;
    private final String data ;

    public Mensagem(String user_origem, String user_destino, String mensagem, String data) {
        this.user_origem = user_origem;
        this.user_destino = user_destino;
        this.mensagem = mensagem;
        this.data = data;
    }

    public String getData() {
        return data;
    }
    
    public String getUser_origem() {
        return user_origem;
    }

    public String getUser_destino() {
        return user_destino;
    }

    public String getMensagem() {
        return mensagem;
    }
    
}
