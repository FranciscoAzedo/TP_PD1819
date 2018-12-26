
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pd1819?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    
    //username e password para aceder à base de dados
    private static final String USER = "Francisco";
    private static final String PASS = "12345";
    
    private static Connection conn = null;
    private static Statement stmt = null;
    private HashMap <Socket, String> clientes;
    private HashMap <Socket, String> updates;
    
    protected ServerSocket serverSocket;
    
    public static final int TCP_PORT = 5001;
    public static final int UDP_PORT = 6001;

    public Servidor() {
        clientes = new HashMap<>();
        updates = new HashMap<>();
        
        try {
            serverSocket = new ServerSocket(TCP_PORT);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro a iniciar o servidor:\n\t"+e);
        }
    }
   
    public void lancaThreads(){
        Thread t = new Check_Alive(this);
        t.setDaemon(true);
        t.start();
    }
    
    //funcao que recebe ligacoes tcp e lança threads para atender cientes
    public void receberClientes(){
        boolean igual;
        while(true){
            igual = false;
            try {                 
                Socket cliente = serverSocket.accept();
                for(Socket s : clientes.keySet()){
                    if (s.getInetAddress().equals(cliente.getInetAddress())){
                        updates.put(cliente, clientes.get(s));
                        igual = true;
                    }
                }
                if (!igual){
                    Thread t = new AtendeCliente(cliente, this);
                    t.setDaemon(true);
                    t.start();
                }
            } catch (IOException e) {
                System.out.println("Erro receber cleintes servidor");
            }
        }
    }
    
    public void atualizarClientes(String tipo){
        ObjectOutputStream out = null;
        for(Socket c : updates.keySet()){
            try {
                out = new ObjectOutputStream(c.getOutputStream());
                out.writeObject(tipo);
                out.flush();
            } catch (IOException e) {
                System.out.println("Ocorreu um erro atualizar clientes servidor: Falha a enviar atualização para " + updates.get(c));                
            }
        }
        
    }
    
    //funcao verificar se nome utilizador ja existe
    public int verificarUsername(String username){
        
        try {
            String sql = "SELECT * FROM Utilizadores";
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                if (rs.getString("Username").equalsIgnoreCase(username))
                    return -1;
            }            
        } catch (SQLException se) {
                System.out.println("Erro verificar username servidor");
            return -2;
        }
        return 1;
    }
    
    //funcao para registar utilizador
    public int registarUser(String username, String password){
        try {
            String sql = "INSERT INTO Utilizadores (Username, Password) VALUES (\"" + username + "\", \"" + password + "\");";
            stmt.executeUpdate(sql);          
        } catch (SQLException se) {
                System.out.println("Erro registar user servidor");
            return -2;
        }
        return 1;
    }
    
    public void efetuarLogout(Socket s){
        try {
            String sql = "UPDATE Utilizadores SET Online = 0, IP = NULL WHERE Username = \"" + clientes.get(s) + "\"";
            stmt.executeUpdate(sql);
            for (Iterator<Socket> it = updates.keySet().iterator(); it.hasNext();) {
                Socket u = it.next();
                if(u.getInetAddress().equals(s.getInetAddress()))
                    it.remove();
            }
            atualizarClientes("Utilizadores");
            Thread t = new Send_Updates_UDP(utilizadoresOutrosServidores(), "Utilizadores");
            t.setDaemon(true);
            t.start();
        } catch (SQLException e) {
                System.out.println("Erro efetuar logout servidor");
        }
        clientes.remove(s);
    }
    
    //funcao para efetuar login
    public int efetuarLogin(String username, String password, Socket s){
        try {
            String sql = "SELECT * FROM Utilizadores WHERE Username = \"" + username + "\"";
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next())
                return -1;
            else
                if (!rs.getString("password").equalsIgnoreCase(password))
                    return -3;
                else{
                    if (rs.getInt("Online") == 1)
                        return -4;
                    else{
                        String ip = s.getInetAddress().toString().replace("/", "");
                        sql = "UPDATE Utilizadores SET Online = 1, IP = \"" + ip + "\" WHERE Username = \"" + username + "\"";
                        stmt.executeUpdate(sql);
                        clientes.put(s, username);
                        atualizarClientes("Utilizadores");
                        Thread t = new Send_Updates_UDP(utilizadoresOutrosServidores(), "Utilizadores");
                        t.setDaemon(true);
                        t.start();
                    }
                }            
        } catch (SQLException se) {
                System.out.println("Erro efetuar login servidor");
            return -2;
        }
        return 1;
    }

    public ArrayList<Mensagem> getMensagens(String user_1, String user_2){
        ArrayList<Mensagem> mensagens = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Mensagens "
                    + "WHERE (origem = \"" + user_1 + "\" AND detino = \"" + user_2 + "\")"
                    + "OR (origem = \"" + user_2 + "\" AND detino = \"" + user_1 + "\")";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                mensagens.add(new Mensagem(rs.getString("origem"), rs.getString("destino"), rs.getString("mensagem"), rs.getDate("data")));
            }
        } catch (SQLException se) {
                System.out.println("Erro get mensagens servidor");
        }
        return mensagens;
    }
    
    public String getIP(String username){
        try {
            String sql = "SELECT * FROM Utilizadores WHERE Username = \"" + username + "\"";
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next())
                return null;
            else{
                if(rs.getInt("Online") == 0)
                    return null;
                else{
                    String ip = rs.getString("IP");
                    ip = ip.replace("/", "");
                    return ip;
                }
            }                
        } catch (SQLException se) {
            System.out.println("Erro efetuar login servidor");
            return null;
        }
    }
    
    public int escreverMensagem(Mensagem msg){
        if(registarMensagem(msg) == 0)
            return -1; //Erro na BD
        else{
            if(updates.values().contains(msg.getUser_destino())){
                for(Map.Entry<Socket, String> entry : updates.entrySet()) {
                    if(entry.getValue().equals(msg.getUser_destino())){
                        try{
                            Socket s = entry.getKey();
                            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                            out.writeObject(msg);
                            out.flush();
                        } catch (IOException e) {
                            System.out.println("Erro a enviar mensagem TCP servidor");
                            return -2; //Erro socket
                        }
                        break;
                    }
                }   
            }
            else{
                String ip;
                if((ip = getIP(msg.getUser_destino())) != null){
                    try{
                        DatagramSocket socket = new DatagramSocket();
                        socket.setSoTimeout (5 * 1000);
                        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(bOut);
                        out.writeObject(msg);
                        out.flush();
                        DatagramPacket packet = new DatagramPacket(bOut.toByteArray(), bOut.size(), InetAddress.getByName(ip), UDP_PORT);
                        socket.send(packet);
                    } catch (SocketException ex) {
                        System.out.println("Erro a enviar mensagem UDP servidor");
                        return -2; //Erro socket
                    } catch (IOException ex) {
                        System.out.println("Erro a enviar mensagem UDP servidor");
                        return -2; //Erro socket
                    }
                }
                else
                    return -3; //user desconectado
            }
        }
        return 1;
    }
    
    public int registarMensagem(Mensagem msg){
        try {
            String sql = "INSERT INTO Mensagens (Origem, Mensagem, Destino, Data) VALUES (\"" + msg.getUser_origem() + "\", \"" + msg.getMensagem() + "\", \"" + msg.getUser_destino() + "\", " + msg.getData() + ");";
            System.out.println(sql);
            stmt.executeUpdate(sql);          
        } catch (SQLException se) {
                System.out.println("Erro escrever mensagem servidor");
            return 0;
        }
        return 1;
    }
    
    
    //funcao para retornar utilizadores online
    public ArrayList<String> utilizadoresOnline(String username){
        ArrayList<String> utilizadores = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Utilizadores";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                if (rs.getInt("Online") == 1 && !rs.getString("Username").equalsIgnoreCase(username))
                    utilizadores.add(rs.getString("Username")); 
            }
        } catch (SQLException se) {
                System.out.println("Erro utilizadores online servidor");
        }
        return utilizadores;
    }
    
    public HashMap<String, String> utilizadoresOutrosServidores(){
        HashMap<String, String> utilizadores = new HashMap<>();
        try {
            String sql = "SELECT * FROM Utilizadores WHERE Online = 1";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                //rs.getInt("Online") == 1 && 
                if (!clientes.values().contains(rs.getString("Username")))
                    utilizadores.put(rs.getString("IP"), rs.getString("Username")); 
            }
        } catch (SQLException se) {
                System.out.println("Erro utilizadores outros servidores servidor");
        }
        return utilizadores;
    }
    
    public void desconectarUtilizador(String ip){
     try {
            String sql = "UPDATE Utilizadores SET Online = 0, IP = NULL, Falhas = 0 WHERE IP = \"" + ip + "\"";
            stmt.executeUpdate(sql); 
        } catch (SQLException e) {
                System.out.println("Erro desconectar utilizador servidor");
        }   
    }
    
    public int getFalhas(String ip){
        int falhas = -1;
        try {
            String sql = "SELECT * FROM Utilizadores WHERE IP = \"" + ip + "\"";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
                falhas = rs.getInt("Falhas"); 
        } catch (SQLException se) {
                System.out.println("Erro get falhas servidor");
        }
        return falhas;
    }
    
    public void atualizarFalhas(String ip, int falhas){
        try {
            String sql = "UPDATE Utilizadores SET Falhas = " + falhas + " WHERE IP = \"" + ip + "\"";
            stmt.executeUpdate(sql); 
        } catch (SQLException e) {
                System.out.println("Erro atualizar falhas servidor");
        }
    }
    
    public void anotarFalha(String ip){
        System.out.println("Anotei falha no ip:" + ip);
        int falhas = getFalhas(ip);
        if (falhas < 3){
            falhas ++;
            atualizarFalhas(ip, falhas);
        }
        else{
            desconectarUtilizador(ip);
        }
    }
    
    public static void main(String[] args) {
 
        try{
            //STEP 2: Register JDBC driver
            //Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            
            Servidor server = new Servidor();
            server.lancaThreads();
            server.receberClientes();
            
        }catch(SQLException se){
                System.out.println("Erro main servidor");
        }
        finally{
            try{
                if(stmt!=null) 
                    stmt.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
            try{
                if(conn!=null) 
                    conn.close();
            }catch(SQLException se){ 
                se.printStackTrace();
            }
        }
    }
}
