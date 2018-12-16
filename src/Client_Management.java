
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client_Management extends java.util.Observable {
    
    protected String username;
    public static final int TIMEOUT = 10; //segundos
    public static final int SERVER_PORT = 5001;
    public static final String IP = "localhost";
    protected static Socket socket;
    protected static ObjectInputStream in = null;
    protected static ObjectOutputStream out = null;

    public Client_Management() {
        
        try {
            socket = new Socket(InetAddress.getByName(IP), SERVER_PORT);
            socket.setSoTimeout(TIMEOUT*1000);   
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }
    }
    
    public Pedido_Registo preencherDados(Pedido_Registo p){    
        try{
            out.writeObject(p);
            out.flush();
            p = (Pedido_Registo) in.readObject();
            return p;
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client_Management.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Pedido_Utilizadores getUtilizadores(){
        
        try {
            Pedido_Utilizadores p = new Pedido_Utilizadores(username);
            out.writeObject(p);
            out.flush();
            p =  (Pedido_Utilizadores) in.readObject();
            return p;
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void login(String username){
        this.username = username;
        try {
            socket = new Socket(InetAddress.getByName(IP), SERVER_PORT);
            socket.setSoTimeout(TIMEOUT*1000);   
//            out = new ObjectOutputStream(socket.getOutputStream());
//            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }
    }
    
    public void logout(){
        try {
            out.writeObject("logout");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }
        username = null;
    }
    
}
