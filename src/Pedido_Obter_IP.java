
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Francisco
 */
public class Pedido_Obter_IP implements Serializable{
    protected String username;
    protected String IP;

    public Pedido_Obter_IP(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getIP() {
        return IP;
    }    
}
