
import java.util.Date;

public class Mensagem {
    private final String user_origem;
    private final String user_destino;
    private final String mensagem;
    private final Date data ;

    public Mensagem(String user_origem, String user_destino, String mensagem, Date data) {
        this.user_origem = user_origem;
        this.user_destino = user_destino;
        this.mensagem = mensagem;
        this.data = data;
    }

    public Date getData() {
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
