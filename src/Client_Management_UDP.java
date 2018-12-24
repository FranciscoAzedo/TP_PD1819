
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

public class Client_Management_UDP extends Thread{

    public static final int SERVER_PORT = 5001;
    public static final int MAX_SIZE = 10000;
    
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Client_Management CM;
    protected static ObjectInputStream in = null;
    protected static ObjectOutputStream out = null;
    protected ByteArrayOutputStream bOut = null;

    public Client_Management_UDP(String IP, Client_Management cm) {
        try {
            CM = cm;
            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            socket = new DatagramSocket(SERVER_PORT, InetAddress.getByName(IP));
        } catch (SocketException e) {
            System.out.println("Erro a criar o socket udp no ip: " + socket.getInetAddress());
        } catch (UnknownHostException e) {
            System.out.println("Erro a criar o socket udp no ip: " + socket.getInetAddress());
        }
    }

    public void run() {
        String pedido;
        if(socket == null)
            return;
        while(true){
            try {
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
                System.out.println("Erro no socket do cliente no ip:" + socket.getInetAddress() + "\nErro:" + e);
            } catch (ClassNotFoundException ex) {
                System.out.println("Erro serializacao no cliente com ip:" + socket.getInetAddress());
            }
        }
    }
}
