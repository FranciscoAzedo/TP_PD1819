
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Scanner;

public class Cliente implements java.util.Observer{
    
    protected static Scanner sc = null;
    protected static Client_Management CM = null;
    protected static ArrayList<String> utilizadores = null;
    protected static ArrayList<Mensagem> mensagens = null;
    protected static boolean menu = false;
    protected static boolean menuListUsers = false;
    protected static boolean menuMensagemUser = false;
    protected static String User = null;
    protected static String User_MSG = null;
    
    
    public static void menuMensagensUtilizador(String username){
        String s;
        Pedido_Obter_Mensagens p = CM.getMensagens(username);
        if(p != null){
            mensagens = p.getMensagens();
            System.out.println("\n\t" + username.toUpperCase() + "\n");
            for(int j = 0; j < mensagens.size(); j++){
                   System.out.println(mensagens.get(j).getUser_origem() + ": " + mensagens.get(j).getMensagem());
               }
            System.out.println("\n(0 - voltar)");
            System.out.print(">> ");
            s = sc.next();
            if(s.equals("0"))
                menuUtilizador(username);
            else{
                Pedido_Escrever_Mensagem pedido = new Pedido_Escrever_Mensagem(new Mensagem(User, username, s, Calendar.getInstance().getTime()));
                CM.escreverMensagem(pedido);
                
            }
        }
        else{
            menuPrincipal();
        }
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
        
        switch(i){
            case 0:
                menuListaUtilizadores();
                break;
            case 1:
                break;
            case 2:
                User_MSG = username;
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
            if(((String) arg).equalsIgnoreCase("Utilizadores")){
                if (menuListUsers){
                    utilizadores = CM.getUtilizadores().getUtilizadores();
                    atualizaUtilizadores();                    
                }
                
            }else if(((String) arg).equalsIgnoreCase("Mensagem")){
                if(menuMensagemUser){
                    mensagens = CM.getMensagens(User_MSG).getMensagens();
                    atualizarMensagens();
                }                
            }else if(((String) arg).equalsIgnoreCase("Utilizadores")){
                
            }else{
                
            }
        }
    }
}
