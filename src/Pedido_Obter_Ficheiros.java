
import java.io.Serializable;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gil Soares
 */
public class Pedido_Obter_Ficheiros implements Serializable {
    ArrayList<String> ficheiros;
    String username;
    String ip;
    public Pedido_Obter_Ficheiros(String user){
        username = user;
    }

    public ArrayList<String> getFicheiros() {
        return ficheiros;
    }

    public void setFicheiros(ArrayList<String> ficheiros) {
        this.ficheiros = ficheiros;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
