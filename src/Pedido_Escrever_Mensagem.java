
import java.io.Serializable;

public class Pedido_Escrever_Mensagem implements Serializable {
    
    private final Mensagem msg;
    private int aprovado = 0; 

    public Pedido_Escrever_Mensagem(Mensagem msg) {
        this.msg = msg;
    }

    public Mensagem getMsg() {
        return msg;
    }
    
    public void setAprovado(int aprovado) {
        this.aprovado = aprovado;
    }

    public int getAprovado() {
        return aprovado;
    }
    
}
