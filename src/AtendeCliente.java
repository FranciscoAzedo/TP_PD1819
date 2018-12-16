
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AtendeCliente extends Thread {
    
    private Socket cliente;
    private static Servidor servidor;
    private ObjectInputStream oin;
    private ObjectOutputStream out;
    
    public AtendeCliente(Socket s, Servidor serv){
        try {
            cliente = s;
            servidor = serv;
            oin = new ObjectInputStream(cliente.getInputStream());
            out = new ObjectOutputStream(cliente.getOutputStream());
        } catch (IOException e) {
                System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }
    } 
    
    public void run(){
        while(!cliente.isClosed()){
            try { 
                Object pedido = oin.readObject();
                if (pedido instanceof Pedido_Registo){                
                    Pedido_Registo p = (Pedido_Registo) pedido;
                    if(p.getTipo().equalsIgnoreCase("Registar")){
                        switch(servidor.verificarUsername(p.getUsername())){
                            case 1:
                                p.resultado_Pedido(servidor.registarUser(p.getUsername(), p.getPassword()));
                                break;
                            case -1:
                                p.resultado_Pedido(-1);
                                break;
                            case -2:
                                p.resultado_Pedido(-2);
                                break;
                        }
                    }
                    else{
                        p.resultado_Pedido(servidor.efetuarLogin(p.getUsername(), p.getPassword(), cliente));
                    }
                    
                    out.writeObject(pedido);
                    out.flush();
                }
                else if(pedido instanceof Pedido_Utilizadores){
                    System.out.println("Pedido Utilizadores");
                    Pedido_Utilizadores p = (Pedido_Utilizadores) pedido;
                    p.setUtilizadores(servidor.utilizadoresOnline(p.getUsername()));
                    out.writeObject(pedido);
                    out.flush();
                }
                else if(pedido instanceof String){
                    if (pedido.equals("logout")){
                        servidor.efetuarLogout(cliente);
                    }
                }
            } catch (IOException e) {
                try {
                    System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
                    servidor.efetuarLogout(cliente);
                    cliente.close();
                } catch (IOException ex) {
                }
            } catch (ClassNotFoundException e) {
                Logger.getLogger(AtendeCliente.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}