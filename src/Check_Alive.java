
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Check_Alive extends Thread {
    
    public static final int SERVER_PORT = 5001;
    public static final int MAX_SIZE = 10000;
    
    private DatagramSocket socket;
    private DatagramPacket packet;
    protected static ObjectInputStream in = null;
    protected static ObjectOutputStream out = null;
    protected ByteArrayOutputStream bOut = null;
    protected Servidor server = null;
    protected HashMap <String, String> utilizadores = new HashMap<>();

    public Check_Alive(Servidor server) {
        this.server = server;
    }
    
    public void run(){
        while(true){
            String resposta, ip;
            try {
                utilizadores.clear();
                utilizadores = server.utilizadoresOutrosServidores();
                Iterator it = utilizadores.keySet().iterator();
                while (it.hasNext()) {
                    try{
                        ip = (String) it.next();
                        DatagramSocket socket = new DatagramSocket();
                        socket.setSoTimeout (5 * 1000);
                        bOut = new ByteArrayOutputStream();
                        out = new ObjectOutputStream(bOut);
                        out.writeObject("Ativo");
                        out.flush();
                        packet = new DatagramPacket(bOut.toByteArray(), bOut.size(), InetAddress.getByName(ip), SERVER_PORT);
                        socket.send(packet);
                        packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                        socket.receive(packet);
                        in = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                        resposta = (String)in.readObject();
                        if(!resposta.equalsIgnoreCase("Ativo"))
                            server.anotarFalha(ip);
//                    } catch (TimeoutException ex) {
//                        Logger.getLogger(Check_Alive.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SocketException ex) {
                        Logger.getLogger(Check_Alive.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Check_Alive.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Check_Alive.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                sleep(5000);
            } catch (InterruptedException e) {}
        }
    }
}
