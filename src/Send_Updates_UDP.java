
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

public class Send_Updates_UDP extends Thread{
    
    public static final int UDP_PORT = 6002;
    
    private DatagramSocket socket;
    private DatagramPacket packet;
    private final String tipo;
    protected static ObjectInputStream in = null;
    protected static ObjectOutputStream out = null;
    protected ByteArrayOutputStream bOut = null;
    protected Servidor server = null;
    protected HashMap <String, String> utilizadores = new HashMap<>();

    public Send_Updates_UDP(HashMap <String, String> utilizadores, String tipo) {
        this.utilizadores = utilizadores;
        this.tipo = tipo;        
    }

    public void run() {
        Iterator it = utilizadores.keySet().iterator();
        String ip = null;
        while (it.hasNext()) {
            try{
                System.out.println(tipo);
                ip = (String) it.next();
                socket = new DatagramSocket();
                socket.setSoTimeout (5 * 1000);
                bOut = new ByteArrayOutputStream();
                out = new ObjectOutputStream(bOut);
                out.writeObject(tipo);
                out.flush();
                packet = new DatagramPacket(bOut.toByteArray(), bOut.size(), InetAddress.getByName(ip), UDP_PORT);
                System.out.println("Enviei " + packet.getData().length + " bytes para o ip " + InetAddress.getByName(ip).toString() + " e para o porto " + UDP_PORT);
                socket.send(packet);
            } catch (IOException e) {
                System.out.println("Erro thread Send Updates UDP ip:" + ip);
            }
        }
    }    
}
