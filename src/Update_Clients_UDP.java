
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

public class Update_Clients_UDP extends Thread{
    
    public static final int SERVER_PORT = 5001;
    
    private DatagramSocket socket;
    private DatagramPacket packet;
    private final String tipo;
    protected static ObjectInputStream in = null;
    protected static ObjectOutputStream out = null;
    protected ByteArrayOutputStream bOut = null;
    protected Servidor server = null;
    protected HashMap <String, String> utilizadores = new HashMap<>();

    public Update_Clients_UDP(HashMap <String, String> utilizadores, String tipo) {
        this.utilizadores = utilizadores;
        this.tipo = tipo;
        
        System.out.println("Utilizadores:" + utilizadores);
        System.out.println("Tipo:" + tipo);
        
    }

    public void run() {
        Iterator it = utilizadores.keySet().iterator();
        String ip = null;
        while (it.hasNext()) {
            try{
                ip = (String) it.next();
                DatagramSocket socket = new DatagramSocket();
                socket.setSoTimeout (5 * 1000);
                bOut = new ByteArrayOutputStream();
                out = new ObjectOutputStream(bOut);
                out.writeObject(tipo);
                out.flush();
                packet = new DatagramPacket(bOut.toByteArray(), bOut.size(), InetAddress.getByName(ip), SERVER_PORT);
                socket.send(packet);
            } catch (IOException e) {
                System.out.println("Erro a atualizar cliente com ip:" + ip);
            }
        }
    }    
}
