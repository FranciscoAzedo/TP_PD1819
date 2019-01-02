
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Scanner;

public class Cliente implements java.util.Observer{
    
    protected static Scanner sc = null;
    protected static Client_Management CM = null;
    public static final String PATH = "C:\\\\Users\\\\franc\\\\Desktop\\\\teste_1";
    protected static ArrayList<String> utilizadores = null;
    protected static ArrayList<Mensagem> mensagens = null;
    protected static boolean menu = false;
    protected static boolean menuListUsers = false;
    protected static boolean menuUser = false;
    protected static boolean menuMensagemUser = false;
    protected static boolean menuFicheirosUser = false;
    protected static String User = null;
    protected static String User_MSG = null;
    
    
    public static void menuMensagensUtilizador(String username){
        String s;
        do{
            Pedido_Obter_Mensagens p = CM.getMensagens(username);
            if(p != null){
                mensagens = p.getMensagens();
                System.out.println("\n\t" + username.toUpperCase() + "\n");
                for(int j = 0; j < mensagens.size(); j++){
                       System.out.println(mensagens.get(j).getUser_origem() + ": " + mensagens.get(j).getMensagem());
                   }
                System.out.println("\n(0 - voltar)");
                System.out.print(">> ");
                s = sc.nextLine();
                if(s.equals("0")){
                    menuMensagemUser = false;
                    menuUser = true;
                    menuUtilizador(User_MSG);
                    break;
                }
                else{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Pedido_Escrever_Mensagem pedido = new Pedido_Escrever_Mensagem(new Mensagem(User, username, s, sdf.format(Calendar.getInstance().getTime())));
                    CM.escreverMensagem(pedido);
                }
            }
            else{
                User_MSG = null;
                menuMensagemUser = false;
                menu = true;
                menuPrincipal();
                break;
            }
        } while(!s.equals("0"));
    }
    
    public static void MenuListaFicheiros(){
        int i;
        Pedido_Obter_Ficheiros p = CM.getFicheiros(User_MSG);
        do{
            System.out.println("\n\t" + User_MSG.toUpperCase() + "\n");
      
            if(p.ficheiros.isEmpty())
                System.out.println("Não existem ficheiros para trnasferencia!");
            else{
                for(int j = 0; j < p.ficheiros.size(); j++){
                    System.out.println(j+1 + " - " + p.ficheiros.get(j));
                }
            }
            
            System.out.println("0 - Anterior");
            System.out.print("\n>> ");
            i = sc.nextInt();
            if (i < 0 || i > p.ficheiros.size()){
                System.out.println("\nOpção inválida\n");
            }
        } while(i < 0 || i > p.ficheiros.size());
        
        if(i>0){
            String [] ficheiro = p.ficheiros.get(i-1).split(" ");
            if(CM.TransferirFicheiros(User_MSG, ficheiro[0], p.getIp()) == 1)
                System.out.println("Transferencia do ficheiro " + ficheiro[0] + " colcuida com sucesso");
            else
                System.out.println("Ocorreu um erro na transferencia do ficheiro!");
        }
        
        menuFicheirosUser = false;
        menuUtilizador(User_MSG);
    }
    
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
        menuUser = false;
        switch(i){
            case 0:
                User_MSG = null;
                menuListUsers = true;
                menuListaUtilizadores();
                break;
            case 1:
                menuFicheirosUser = true;
                MenuListaFicheiros();
                break;
            case 2:
                menuMensagemUser = true;
                menuMensagensUtilizador(username);
                break;
        }
    }
    
    public static void atualizaUtilizadores(){
        if(utilizadores.isEmpty()){
            System.out.println("\n\n\nNão existem utilizadores logados\n");
        }
        else{
            System.out.println("\n\n\nLISTA DE UTILIZADORES\n");
            for(int j = 0; j < utilizadores.size(); j++){
                System.out.println(j+1 + " - " + utilizadores.get(j));
            }
        }
        System.out.println("0 - Anterior");
        System.out.print("\n>> ");
    }
    
    public static void atualizarMensagens(){
        System.out.println("\n\t" + User_MSG.toUpperCase() + "\n");
            for(int j = 0; j < mensagens.size(); j++){
                   System.out.println(mensagens.get(j).getUser_origem() + ": " + mensagens.get(j).getMensagem());
               }
            System.out.println("\n(0 - voltar)");
            System.out.print(">> ");
    }
    
    public static void atualizarFicheiros(ArrayList<String> ficheiros){
        System.out.println("\n\t" + User_MSG.toUpperCase() + "\n");
      
            if(ficheiros.isEmpty())
                System.out.println("Não existem ficheiros para trnasferencia!");
            else{
                for(int j = 0; j < ficheiros.size(); j++){
                    System.out.println(j+1 + " - " + ficheiros.get(j));
                }
            }
            
            System.out.println("0 - Anterior");
            System.out.print("\n>> ");
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
            menuListUsers = false;
            if(i == 0)                
                menuPrincipal();
            else{
                menuUser = true;
                User_MSG = utilizadores.get(i - 1);
                menuUtilizador(User_MSG);
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
            System.out.println("2 - Visualizar histórico de transferências");
            System.out.println("0 - Logout");            
            System.out.print("\n>> ");
            i = sc.nextInt();
            if (i < 0 || i > 2){
                System.out.println("\nOpção inválida\n");
            }
        } while(i < 0 || i > 2);
        menu = false;
        switch(i){
            case 0:                
                CM.logout();
                menuInicial();
                break;
            case 1:
                menuListUsers = true;
                menuListaUtilizadores();
                break;
            case 2:
                listarTransferencias();
                menuPrincipal();
                //ver historico de transferencias
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
                p = new Pedido_Registo(user, password, "Registar", PATH);
                break;
            case 2:
                p = new Pedido_Registo(user, password, "Login", PATH);
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
                        CM.login(user, PATH);
                        User = user;
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

    private static void listarTransferencias() {
        ArrayList<Pedido_Registar_Transferencia> transferencias = CM.getTransferencias(User);
        if(transferencias.isEmpty()){
            System.out.println("\nAinda não fez qualquer transferencia!\n");
        }
        else{
            System.out.println("\nHISTORICO DE TRANSFERENCIAS\n");
            for(int j = 0; j < transferencias.size(); j++){
                System.out.println(transferencias.get(j).getFicheiro() + "  " + transferencias.get(j).getDono() + "  " + transferencias.get(j).getData());
            }
            System.out.println("");
        }
    }
    
    public void start(){
        CM = new Client_Management();
        CM.addObserver(this);
        sc = new Scanner(System.in);
        menuInicial();
    }
    
    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof String){
            String[] parts = ((String)arg).split(" ");
            if((parts[0]).equalsIgnoreCase("Utilizadores")){
                if (menuListUsers){
                    utilizadores = CM.getUtilizadores().getUtilizadores();
                    atualizaUtilizadores();                    
                }
                else if(parts[1].equals(User_MSG)){
                    System.out.println("\n\nUtilzador " + User_MSG + " desconectou-se!\n");
                    if(menuMensagemUser)
                        System.out.println("\n(0 - voltar)");
                    System.out.print(">> ");
                }                
            }else if(parts[0].equalsIgnoreCase("Mensagem")){
                if(menuMensagemUser && parts[1].equals(User_MSG)){
                    mensagens = CM.getMensagens(User_MSG).getMensagens();
                    atualizarMensagens();
                }                
            }else if(parts[0].equalsIgnoreCase("Ficheiros")){
                if(menuFicheirosUser && parts[1].equals(User_MSG)){
                    atualizarFicheiros(CM.getFicheiros(User_MSG).getFicheiros());                    
                }
            }
            else{
                System.out.println("Update desconhecido!");
            }
        }
    }
}
