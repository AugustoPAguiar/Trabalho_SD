package clientes;
 
import formularios.MainForm;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Atribui a cada ligação do cliente uma thread
 * separada e também é capaz de transmitir mensagens 
 * para todos os clientes ligados, mediante alguma
 * regras
 */
public class Client implements Runnable {

    private BufferedReader in;
    private MainForm form;

    public Client(BufferedReader in, MainForm form) {
        this.in = in;
        this.form = form;
    }

    @Override
    public void run() {
        readMessageFrmServer();
    }

    private void readMessageFrmServer() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                final String mensagemFinal = msg; // cria cópia final
                javax.swing.SwingUtilities.invokeLater(() -> {form.appendMessage(mensagemFinal);});
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler mensagens: " + e.getMessage());
        }
    }
}

