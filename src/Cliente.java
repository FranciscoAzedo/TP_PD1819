
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

public class Cliente implements java.util.Observer{
    
    protected static Scanner sc = null;
    protected static Client_Management CM = null;
    protected static ArrayList<String> utilizadores = null;
    protected static boolean menu = false;
    protected static boolean menuListUsers = false;
    
    public static void menuUtilizador(String username){
        int i;
        do{
            System.out.println("\n\t" + username.toUpperCase() + "\n");
            System.out.println("1 - Ver ficheiros disponíveis");
            System.out.println("2 - Enviar mensagem");
            System.out.println("0 - Anterior");            
            System.out.print("\n>> ");
            i = sc.nextInt();
            if (i < 0 || i > 2){
                System.out.println("\nOpção inválida\n");
            }
        } while(i < 0 || i > 2);
    }
    
    public static void menuListaUtilizadores(){
        int i;
        Pedido_Utilizadores p = CM.getUtilizadores();
        
        if(p != null){
            utilizadores = p.getUtilizadores();
            do{
                if(utilizadores.isEmpty()){
                    System.out.println("\nNão existem utilizadores logados\n");
                }
                else{
                    System.out.println("\nLISTA DE UTILIZADORES\n");
                    for(int j = 0; j < utilizadores.size(); j++){
                        System.out.println(j+1 + " - " + utilizadores.get(j));
                    }
                }
                System.out.println("0 - Anterior");
                System.out.print("\n>> ");
                i = sc.nextInt();
                if (i < 0 || i > utilizadores.size()){
                    System.out.println("\nOpção inválida\n");
                }
            } while(i < 0 || i > utilizadores.size());
            
            if(i == 0){
                menuListUsers = false;
                menuPrincipal();
            }
            else{
                menuUtilizador(utilizadores.get(i-1));
            }
        }
        
        else{
            menuListUsers = false;
            System.out.println("\nErro a carregar utilizadores!\n");
            menuPrincipal();
        }
        
    }
    
    public static void menuPrincipal(){
        int i;
        do{
            System.out.println("1 - Ver utilizadores online");
            System.out.println("2 - Alterar diretoria de ficheiros disponíveis");
            System.out.println("3 - Alterar ficheiros disponíveis");
            System.out.println("4 - Alterar diretoria de ficheiros recebidos");
            System.out.println("5 - Visualizar histórico de transferências");
            System.out.println("0 - Logout");            
            System.out.print("\n>> ");
            i = sc.nextInt();
            if (i < 0 || i > 5){
                System.out.println("\nOpção inválida\n");
            }
        } while(i < 0 || i > 5);
        
        switch(i){
            case 0:
                menu = false;
                CM.logout(); //falta implementar a parte de alterar variavel online no server
                menuInicial();
                break;
            case 1:
                menuListUsers = true;
                menuListaUtilizadores();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
        
    }
    
    public static void preencherDados(int operacao){
        String user, password;
        System.out.print("Username: ");
        user = sc.next();
        System.out.print("Password: ");
        password = sc.next();
        Pedido_Registo p = null;
        
        switch(operacao){
            case 1:
                p = new Pedido_Registo(user, password, "Registar");
                break;
            case 2:
                p = new Pedido_Registo(user, password, "Login");
                break;
        }
        
        p = CM.preencherDados(p);
        
            switch(p.getAprovado()){
                case 1:
                    if (p.getTipo().equalsIgnoreCase("Registar")){
                        System.out.println("\nRegisto feito com sucesso!\n");
                    }
                    else{
                        System.out.println("\nLogin efetuado com sucesso!\n");
                        CM.login(user);
                        menu = true;
                    }
                    break;
                case -1:
                    if (p.getTipo().equalsIgnoreCase("Registar"))
                        System.out.println("\nUsername indisponível!\n");
                    else
                        System.out.println("\nUsername inválido!\n");
                    break;
                case -2:
                    System.out.println("\nOcorreu um erro na ligação à base de dados\n");
                    break;
                case -3:
                    System.out.println("\nPassword incorreta!\n");
                    break;
                case -4:
                System.out.println("\nUtilizador já está logado!\n");
                break;
            }
            if(!menu)
                menuInicial();
            else
                menuPrincipal();
    } 
    
    public static void menuInicial(){
        int i;
        do{
            System.out.println("1 - Registar");
            System.out.println("2 - Login");
            System.out.println("0 - Sair");
            System.out.print("\n>> ");
            i = sc.nextInt();
            if (i < 0 || i > 2){
                System.out.println("\nOpção inválida\n");
            }
        } while(i < 0 || i > 2);
        
        if (i > 0)
            preencherDados(i);
    }
    
    public static void main(String[] args) {
        
        CM = new Client_Management();
        sc = new Scanner(System.in);
        menuInicial();        
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
