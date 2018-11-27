/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Francisco
 */

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/pd1819?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    
    static final String USER = "root";
    static final String PASS = "12345";
    
    static Connection conn = null;
    static Statement stmt = null;
    
    public static void main(String[] args) {
 
        try{
            //STEP 2: Register JDBC driver
            //Class.forName(JDBC_DRIVER);
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
//            String sql;
//            sql = "SELECT * FROM City WHERE CountryCode = \"FRA\"";
//            ResultSet rs = stmt.executeQuery(sql);
//            //STEP 5: Extract data from result set
//            while(rs.next()){
//                //Retrieve by column name
//                int id = rs.getInt("id");
//                //int age = rs.getInt("username");
//                String name = rs.getString("name");
//                String population = rs.getString("population");
//                //Display values
//                System.out.print("ID: " + id);
//                //System.out.print(", Age: " + age);
//                System.out.print(", Username: " + name);
//                System.out.println(", Password: " + population);
//            }
            //STEP 6: Clean-up environment
//            rs.close();
            System.out.println(verificarUsername("ze"));
            System.out.println(registarUser("ze", "pass"));
            System.out.println(efetuarLogin("ze", "pass"));
            stmt.close(); 
            conn.close();
        }catch(SQLException se){
        se.printStackTrace();
        }catch(Exception e){
        e.printStackTrace();
        }finally{
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){}
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){ se.printStackTrace();}
        }
    }
    
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
