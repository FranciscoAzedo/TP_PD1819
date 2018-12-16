
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    
    public static final int SERVICE_PORT = 5001;

    public Servidor() {
        clientes = new HashMap<>();
        updates = new HashMap<>();
        try {
            serverSocket = new ServerSocket(SERVICE_PORT);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro a iniciar o servidor:\n\t"+e);
        }
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
                        //ObjectOutputStream objectOutputStream = new ObjectOutputStream(cliente.getOutputStream());
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
                System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
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
                System.out.println("Ocorreu um erro a enviar atualização para " + updates.get(c));                
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
            System.out.println(se);
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
            System.out.println(se);
            return -2;
        }
        return 1;
    }
    
    public void efetuarLogout(Socket s){
        try {
            String sql = "UPDATE Utilizadores SET Online = 0 WHERE Username = \"" + clientes.get(s) + "\"";
            stmt.executeUpdate(sql);
            for (Iterator<Socket> it = updates.keySet().iterator(); it.hasNext();) {
                Socket u = it.next();
                if(u.getInetAddress().equals(s.getInetAddress()))
                    it.remove();
            }
            atualizarClientes("Utilizadores");
        } catch (SQLException e) {
            System.out.println(e);
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
                        sql = "UPDATE Utilizadores SET Online = 1 WHERE Username = \"" + username + "\"";
                        stmt.executeUpdate(sql);
                        clientes.put(s, username);
                        atualizarClientes("Utilizadores");
                    }
                }            
        } catch (SQLException se) {
            System.out.println(se);
            return -2;
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
            System.out.println(se);
        }
        return utilizadores;
    }
    
    public static void main(String[] args) {
 
        try{
            //STEP 2: Register JDBC driver
            //Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            
            new Servidor().receberClientes();
            
        }catch(SQLException se){
            se.printStackTrace();
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
