
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client_Update_TCP extends Thread {
    
    private Socket server;
    private Client_Management CM;
    private ObjectInputStream oin;
    
    public Client_Update_TCP(Socket s, Client_Management cm){
            server = s;
            CM = cm;
    }
    
    public void run(){
        while(!server.isClosed()){
            try {
                oin = new ObjectInputStream(server.getInputStream());
                Object update = oin.readObject();
                if (update instanceof String)
                    CM.update((String) update);
                else if(update instanceof Mensagem)
                    CM.update("Mensagem");
            } catch (IOException e) {
                try {
                    System.out.println("Erro thread Client Update TCP"); 
                    server.close();
                } catch (IOException ex) {
                }
            } catch (ClassNotFoundException e) {
                Logger.getLogger(AtendeCliente.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
}
