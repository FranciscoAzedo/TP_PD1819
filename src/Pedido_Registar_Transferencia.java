
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
public class Pedido_Registar_Transferencia implements Serializable{
    private final String username;
    private final String ficheiro;
    private final String dono;
    private final String data;

    public Pedido_Registar_Transferencia(String username, String ficheiro, String dono, String data) {
        this.username = username;
        this.ficheiro = ficheiro;
        this.dono = dono;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getDono() {
        return dono;
    }

    public String getUsername() {
        return username;
    }

    public String getFicheiro() {
        return ficheiro;
    }
    
}
