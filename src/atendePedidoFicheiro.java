
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gil Soares
 */
public class atendePedidoFicheiro extends Thread {
    private Socket ss;
    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 60;
    
    public atendePedidoFicheiro(Socket socket) {
        ss = socket;
    }
    
    public void run(){
        File localDirectory;
        String fileName = null;
        int nbytes;
        byte[] ficheiro = new byte[MAX_SIZE];
        ObjectInputStream input = null;
        OutputStream out;
        String requestedCanonicalFilePath;
        FileInputStream requestedFileInputStream = null;
        
        try {
            input = new ObjectInputStream(ss.getInputStream());
            fileName = (String)input.readObject();
        } catch (IOException ex) {
            Logger.getLogger(atendePedidoFicheiro.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(atendePedidoFicheiro.class.getName()).log(Level.SEVERE, null, ex);
        }
        fileName = fileName.trim();
        localDirectory = new File("COLOCAR DIRETORIA".trim());
               
        try{
            try{
                ss.setSoTimeout(TIMEOUT*1000);
                   
                out = ss.getOutputStream();

                requestedCanonicalFilePath = new File(localDirectory+File.separator+fileName).getCanonicalPath();

                if(!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath()+File.separator)){
                    System.out.println("Nao e' permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
                    System.out.println("A directoria de base nao corresponde a " + localDirectory.getCanonicalPath()+"!");
                }

                requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath);
                System.out.println("Ficheiro " + requestedCanonicalFilePath + " aberto para leitura.");

                while((nbytes = requestedFileInputStream.read(ficheiro))>0){                        

                    out.write(ficheiro, 0, nbytes);
                    out.flush();

                }     

                System.out.println("Transferencia concluida");

            }catch(UnknownHostException e){
                 System.out.println("Destino desconhecido:\n\t"+e);
            }catch(NumberFormatException e){
                System.out.println("O porto do servidor deve ser um inteiro positivo:\n\t"+e);
            }catch(SocketTimeoutException e){
                System.out.println("Nao foi recebida qualquer bloco adicional, podendo a transferencia estar incompleta:\n\t"+e);
            }catch(SocketException e){
                System.out.println("Ocorreu um erro ao nivel do socket TCP:\n\t"+e);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(atendePedidoFicheiro.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(atendePedidoFicheiro.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }finally{
            
            if(ss != null){
                try {
                    ss.close();
                } catch (IOException ex) {
                    Logger.getLogger(atendePedidoFicheiro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
    }
    
}
