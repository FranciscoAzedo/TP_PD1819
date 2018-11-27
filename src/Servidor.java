
import java.sql.*;

public class Servidor {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/pd1819?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    
    //username e password para aceder à base de dados
    static final String USER = "root";
    static final String PASS = "12345";
    
    static Connection conn = null;
    static Statement stmt = null;
    
    public static void main(String[] args) {
 
        try{
            //STEP 2: Register JDBC driver
            //Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            
            System.out.println(verificarUsername("ze"));
            System.out.println(registarUser("ze", "pass"));
            System.out.println(efetuarLogin("ze", "pass"));
            stmt.close(); 
            conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }
        finally{
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){}
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){ se.printStackTrace();}
        }
    }
    
    //funcao verificar se nome utilizador ja existe
    public static boolean verificarUsername(String username){
        try {
            String sql = "SELECT * FROM Utilizadores";
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                if (rs.getString("username").equalsIgnoreCase(username))
                    return false;
            }            
        } catch (SQLException se) {
            System.out.println(se);
            return false;
        }
        return true;
    }
    
    //funcao para registar utilizador
    public static boolean registarUser(String username, String password){
        try {
            String sql = "INSERT INTO Utilizadores (Username, Password) VALUES (\"" + username + "\", \"" + password + "\");";
            stmt.executeUpdate(sql);          
        } catch (SQLException se) {
            System.out.println(se);
            return false;
        }
        return true;
    }
    
    //funcao para efetuar login
    public static int efetuarLogin(String username, String password){
        try {
            String sql = "SELECT * FROM Utilizadores WHERE Username = \"" + username + "\"";
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next())
                return -1;
            else
                if (!rs.getString("password").equalsIgnoreCase(password))
                    return -2;            
        } catch (SQLException se) {
            System.out.println(se);
            return -3;
        }
        return 1;
    }    
}
