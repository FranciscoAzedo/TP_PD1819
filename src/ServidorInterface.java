
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface ServidorInterface extends Remote {
    public ArrayList<String> utilizadoresOnline(String username) throws RemoteException;
    public ArrayList<String> getFicheiros(String username) throws RemoteException;
}
