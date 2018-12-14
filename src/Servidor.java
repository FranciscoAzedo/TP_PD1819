
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
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
    
    protected ServerSocket serverSocket;
    
    public static final int SERVICE_PORT = 5001;

    public Servidor() {
        try {
            serverSocket = new ServerSocket(SERVICE_PORT);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro a iniciar o servidor:\n\t"+e);
        }
    }
   
    //funcao que recebe ligacoes tcp e lança threads para atender cientes
    public void receberClientes(){
        while(true){
            try {
                Thread t = new AtendeCliente(serverSocket.accept(), this);
                t.setDaemon(true);
                t.start();
            } catch (IOException e) {
                System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
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
    
    //funcao para efetuar login
    public int efetuarLogin(String username, String password){
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

class AtendeCliente extends Thread {
    
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
                        p.resultado_Pedido(servidor.efetuarLogin(p.getUsername(), p.getPassword()));
                    }
                }
                else if(pedido instanceof Pedido_Utilizadores){
                    System.out.println("Pedido Utilizadores");
                    Pedido_Utilizadores p = (Pedido_Utilizadores) pedido;
                    p.setUtilizadores(servidor.utilizadoresOnline(p.getUsername()));
                }

                out.writeObject(pedido);
                out.flush();

            } catch (IOException e) {
                try {
                    System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
                    cliente.close();
                } catch (IOException ex) {
                    System.out.println("Ocorreu um erro a fechar o socket:\n\t"+ex);
                }
            } catch (ClassNotFoundException e) {
                Logger.getLogger(AtendeCliente.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
