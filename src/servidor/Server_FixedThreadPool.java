package servidor;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

/**
 * Atribui a cada ligação do cliente uma thread
 * separada e também é capaz de transmitir/receber mensagens 
 * para/de todos os clientes ligados, mediante alguma
 * regras
 */
public class Server_FixedThreadPool implements Runnable{
    
    // Lista de todos os alunos que se encontram na reuniao do SD Teams
    public static HashSet<Server_FixedThreadPool> listaAlunos = new HashSet<>();
    
    private BufferedReader in;
    private PrintWriter out;
    private Socket clientSocket;
    
    /*Nickname do cliente que entrou na reuniao*/ 
    private String nickname; 

    
    /**
     * Construtor que inicializa o socket cliente
     * @param clientSocket Socket cliente
     */
    public Server_FixedThreadPool(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    

    @Override
    public void run() {
        transferData(clientSocket);
    }

    private void transferData(Socket clientSocket) {
        try {
            in = new BufferedReader(new
                 InputStreamReader(clientSocket.getInputStream()));
            
            out = new PrintWriter(clientSocket.getOutputStream(),true);
            
            //é lido o nickname do utilizador
            nickname = in.readLine();
            
            if(isNicknameInUse(this.nickname)){
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                
                out.println("NICKNAME_NAO_ACEITE");
                clientSocket.close(); // encerra a conexao
                return;
            }            
            
            System.out.println("Aluno " + nickname + " entrou na reuniao");
            
            listaAlunos.add(this);
            atualizarListaAlunos();
            sendCurrentUserListToNewClient();
            broadcastNewUser(this.nickname);
            
            String menu = "\n ********* BEM VINDO AO SD_TEAMS *********\n"
                    + "* Pode conversar com os outros membros da reuniao *\n"
                    + "*  - Utilize @nickname \"mensagem\" para envio de mensagens privadas*\n"
                    + "*  - Utilize \\SAIR para abandonar a sala*\n" 
                    + "******************************************";
            
            out.println(menu);
            
            
            String mensagemDoEstudante;
            
            while((mensagemDoEstudante = in.readLine()) != null){
                System.out.println("De [" + nickname.toUpperCase() + "]: " + mensagemDoEstudante);
                      
                // obtem o index do espaço
                int indexEspaco = mensagemDoEstudante.indexOf(" ");
                
                //testar se a mensagem é privada
                if(mensagemDoEstudante.startsWith("@")){
                    // testar se o index é válido
                    if(indexEspaco != -1){
                        String nicknameDestinatario = mensagemDoEstudante.substring(1, indexEspaco);
                        
                        String mensagemPrivada = mensagemDoEstudante.substring(indexEspaco + 1);
                    
                        sendPrivateMessage(nicknameDestinatario, "De [" + nickname.toUpperCase() + "]: " + mensagemPrivada);
                    }
                }
                //senão, é uma mensagem pública
                else {
                    broadcastMessage("[" + nickname.toUpperCase() + "]: " + mensagemDoEstudante);
                }
            }
            
            
        } catch (IOException ex) {
            System.out.println("Erro de comnunicacao com o cliente");
        }    
        
    }
    
    private void sendCurrentUserListToNewClient() {
        StringBuilder lista = new StringBuilder("LISTA:");

        synchronized(listaAlunos) {
            for (Server_FixedThreadPool aluno : listaAlunos) {
                lista.append(aluno.nickname).append(";");
            }
        }

        out.println(lista.toString()); // envia somente para o novo cliente
    }

    private void broadcastNewUser(String nickname) {
        synchronized(listaAlunos) {
            for (Server_FixedThreadPool aluno : listaAlunos) {
                if (!aluno.equals(this)) { // não envia para o próprio novo cliente
                    aluno.out.println("LISTA:" + nickname + ";");
                }
            }
        }
    }
    
    private void atualizarListaAlunos() {
        StringBuilder lista = new StringBuilder("LISTA:");

        synchronized(listaAlunos) {
            for (Server_FixedThreadPool aluno : listaAlunos) {
                lista.append(aluno.nickname).append(";");
            }
        }

        broadcastMessage(lista.toString());
    }


    private void broadcastMessage(String msg) {
    synchronized(listaAlunos) {
        for (Server_FixedThreadPool aluno : listaAlunos) {
            aluno.out.println(msg);
        }
    }
}

    private void sendPrivateMessage(String nicknameDestinatario, String msg) {

    for (Server_FixedThreadPool aluno : listaAlunos) {
        if (aluno.nickname.equalsIgnoreCase(nicknameDestinatario)) {
            aluno.out.println("[Privado] " + msg);
            return;
        }
    }
    // Se não achou o destinatário, devolver aviso ao remetente
    out.println("⚠️ Utilizador \"" + nicknameDestinatario + "\" não encontrado.");
}

    
    /**
     * Verificar se o nickname já está em uso
     * @param nickname
     * @return true caso esteja em uso; false caso contrário
     */
    private boolean isNicknameInUse(String nickname) {
        
        for(Server_FixedThreadPool aluno : listaAlunos){
            if(nickname != null && aluno.nickname.equalsIgnoreCase(nickname)){
                return true;
            }
        }
        
        return false;
        
    }
    
}
