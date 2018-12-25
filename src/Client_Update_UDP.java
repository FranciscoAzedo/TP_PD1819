
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client_Update_UDP extends Thread{

    public static final int UDP_PORT = 6001;
    public static final int MAX_SIZE = 10000;
    
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Client_Management CM;
    protected static ObjectInputStream in = null;
    protected static ObjectOutputStream out = null;
    protected ByteArrayOutputStream bOut = null;

    public Client_Update_UDP(String IP, Client_Management cm) {
        try {
            CM = cm;
            socket = new DatagramSocket(UDP_PORT, InetAddress.getByName(IP));
        } catch (SocketException e) {
                    System.out.println("Erro construtor Client Update UDP");
        } catch (UnknownHostException e) {
                    System.out.println("Erro construtor Client Update UDP");
        }
    }

    public void run() {
        String pedido;
        if(socket == null)
            return;
        while(true){
            try {
                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);
                in = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                pedido = (String)in.readObject();
                if(pedido != null){
                    if (pedido.equalsIgnoreCase("Ativo")){
                        bOut = new ByteArrayOutputStream(MAX_SIZE);
                        out = new ObjectOutputStream(bOut);
                        out.writeObject("Ativo");
                        out.flush();
                        packet.setData(bOut.toByteArray());
                        packet.setLength(bOut.size());
                        socket.send(packet);
                    }
                    else
                        CM.update(pedido);
                }
                else
                    continue;
            } catch (IOException e) {
                    System.out.println("Erro thread Client Update UDP:" + e);
            } catch (ClassNotFoundException ex) {
                    System.out.println("Erro thread Client Update UDP");
            }
        }
    }
}
