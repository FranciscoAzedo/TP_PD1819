
import java.io.Serializable;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Francisco
 */
public class Pedido_Obter_Transferencias implements Serializable{
    protected final String username;
    protected ArrayList<Pedido_Registar_Transferencia> transferencias;

    public Pedido_Obter_Transferencias(String username) {
        this.username = username;
    }

    public void setTransferencias(ArrayList<Pedido_Registar_Transferencia> transferencias) {
        this.transferencias = transferencias;
    }

    public ArrayList<Pedido_Registar_Transferencia> getTransferencias() {
        return transferencias;
    }

    public String getUsername() {
        return username;
    }
}
