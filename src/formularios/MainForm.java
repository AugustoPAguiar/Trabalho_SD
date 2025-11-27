package formularios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.DefaultListModel;
import clientes.Client;

public class MainForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainForm.class.getName());

    /** Porta servidor à qual o cliente se irá ligar  */
    protected  int porta;
    
    /** Endereço IP do ser servidor ao qual o cliente se irá ligar  */
    protected String ip;
    
    /** Stream de entrada para leitura de mensagens do servidor  */
    protected  BufferedReader in;
    
    /** Stream de saída para envio de mensagens ao servidor  */
    protected  PrintWriter out;
    
     /** Nome de utilizador (nickname)do utilizador */
    protected String nickname;
    
    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
    }
    
    /**
     * Estabelecer a monicação com o servidor através de envio 
     * e receção de mensagens
     */
    public void setupConnection(){
        
        try{
            Socket socket = new Socket(ip, porta);
            
            System.out.println("Ligacao estabelecida com servidor: "
                    + socket.getRemoteSocketAddress());
            
         out = new PrintWriter(socket.getOutputStream(),true);
         
         //envia o nickname do utilizador ao servidor para verificar
         //se poderá entrar na reunião (se não existir nenhum com o mesmo)
         out.println(nickname);
         
         
         in = new BufferedReader(
                 new InputStreamReader(socket.getInputStream()));
                 
         //leitura da resposta do servidor
         String respostaAceitaNickname = in.readLine();
         if(respostaAceitaNickname!=null && 
                 respostaAceitaNickname.contains("NICKNAME_NAO_ACEITE")){
             System.out.println(respostaAceitaNickname);
             closeAll();
             return;
         }
         
            DefaultListModel listModel = new DefaultListModel();
            listModel.addElement(nickname);
            jList_nicknames.setModel(listModel);
     
//         readMessage();
//         
//         sendMessage();
         
        }catch(UnknownHostException e){
             System.out.println("Erro: servidor desconhecido "+
                     ip + e.getMessage());
        }catch(IOException e){
             System.out.println("Erro: configuração socket errada: " +
                      e.getMessage());
        }
    }
    
    /**
     * Fecha as streams de entrada e saída
     */
    private void closeAll() {
        try {
            if(in!=null){
                in.close();
            }
            
            if(out!=null){
                out.close();
            }
            
        } catch (IOException e) {
            System.err.println("Erro a fechar as ligacoes: "
                    + e.getMessage());
        }
        
    }
    
    /**
     * Envia mensagens do cliente - utilizador - para o servidor
     */
    private void  sendMessage(){
        try{
           BufferedReader userInput = 
                   new BufferedReader(new InputStreamReader(System.in));
           
           String linha;
           
            while ((linha = userInput.readLine())!=null) {                
                if(linha.equalsIgnoreCase("\\SAIR")){
                    out.println("["+nickname+"]"+ "saiu da reuniao");
                    //[ana] saiu da reuniao
                 }
            }
        }catch(IOException e){
             System.out.println("Erro no envio da mensagem: " +
                      e.getMessage());
        }finally{
            closeAll();
        }
    }
    
    /**
     * Lê mensagens do servidor numa thread separada por cada cliente e imprime na consola
     */
    private void readMessage(){
        Thread t = new Thread(new Client(in));
        t.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_mensagem = new javax.swing.JTextArea();
        jButton_Enviar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList_nicknames = new javax.swing.JList<>();
        jLabel_contatos = new javax.swing.JLabel();
        jLabel_mensagens = new javax.swing.JLabel();
        jScrollPane_mensagens = new javax.swing.JScrollPane();
        jTextArea_conversas = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SD_Teams");
        setBackground(new java.awt.Color(0, 102, 102));
        setForeground(java.awt.Color.gray);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTextArea_mensagem.setColumns(20);
        jTextArea_mensagem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea_mensagem.setRows(5);
        jScrollPane1.setViewportView(jTextArea_mensagem);

        jButton_Enviar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton_Enviar.setText("Enviar");
        jButton_Enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EnviarActionPerformed(evt);
            }
        });

        jList_nicknames.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jList_nicknames.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_nicknames.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jList_nicknames);

        jLabel_contatos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel_contatos.setText("Contatos");

        jLabel_mensagens.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel_mensagens.setText("Mensagens");

        jScrollPane_mensagens.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTextArea_conversas.setColumns(20);
        jTextArea_conversas.setRows(5);
        jTextArea_conversas.setEnabled(false);
        jScrollPane_mensagens.setViewportView(jTextArea_conversas);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_mensagens, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_contatos, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton_Enviar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane_mensagens, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_contatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_mensagens, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(jScrollPane_mensagens))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jButton_Enviar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_EnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EnviarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_EnviarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        LoginForm loginForm = new LoginForm(this);
        loginForm.setVisible(true);
    }//GEN-LAST:event_formWindowOpened

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Enviar;
    private javax.swing.JLabel jLabel_contatos;
    private javax.swing.JLabel jLabel_mensagens;
    private javax.swing.JList<String> jList_nicknames;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane_mensagens;
    private javax.swing.JTextArea jTextArea_conversas;
    private javax.swing.JTextArea jTextArea_mensagem;
    // End of variables declaration//GEN-END:variables
}
