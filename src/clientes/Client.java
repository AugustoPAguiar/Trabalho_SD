package clientes;
 
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Atribui a cada ligação do cliente uma thread
 * separada e também é capaz de transmitir mensagens 
 * para todos os clientes ligados, mediante alguma
 * regras
 */
public class Client implements Runnable{
    private BufferedReader in;

    
    public Client(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        readMessageFrmServer(in);
    }

    
   /**
 * Le as linhas de texto que estao a ser recebidas DO servidor
 * @param in BufferedReader
 */
    private void readMessageFrmServer(BufferedReader in) {
        String msgDoServidor;
        
        try {
            //enquanto não for lido todo o texto do buffer
            while ((msgDoServidor = in.readLine())!=null) {                
                System.out.println(msgDoServidor);
            }
        } catch (IOException e) {
            System.out.println("Erro na leitura de mensagens do "
                    + "servidor: " + e.getMessage());
        }
    }
    
}
