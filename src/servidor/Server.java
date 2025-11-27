package servidor;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    
    /** Porta Server à qual o cliente se irá ligar  */
    protected int porta;

    /** Construtor com parametros
     * @param porta Porta servidor à qual o cliente se irá ligar 
     */
     public Server(int porta) {
        this.porta = porta;
    }

    /** Construtor por defeito, com porta 12345 */
    public Server() {
        this.porta = 12345;
    }
 
    
    /**
     * Método que inicia o Server e aceita as ligacoes dos clientes
Cada cliente será tratado numa pool de threads
     */
     public void acceptConnections(){
         try {
             ServerSocket serverSocket = new ServerSocket(porta);
             
             System.out.println("Servidor com IP " 
                     + InetAddress.getLocalHost().getHostAddress()+
                     " iniciado na porta " + porta);
             
              System.out.println("A reuniao foi iniciada pelo moderador");
             
              //Criacao de uma pool de threads para tratamento
              //de multiplos clientes
              //Neste caso 10 é o numero de clientes simultaneos
              ExecutorService pool = Executors.newFixedThreadPool(10);
              
              //Loop infinito para aceitar clientes
              while (true) {                 
                 Socket clientSocket = serverSocket.accept();
                 
                 Server_FixedThreadPool serverTP =
                         new Server_FixedThreadPool(clientSocket);
                 pool.execute(serverTP);
             }
              
         } catch (Exception e) {
         }
     }
     
     
     
     /**
     * Metodo principal que inicia o Server
     * @param args Argumentos da inha de comando
     */
    public static void main(String[] args) {
        Server server = new Server(12345);
        server.acceptConnections();
    }
}
