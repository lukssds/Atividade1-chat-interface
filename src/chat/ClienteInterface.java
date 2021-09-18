package chat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class ClienteInterface extends javax.swing.JFrame {

    Socket conexao;

    private static BufferedReader entrada;
    private static  PrintStream  saida;
    private String  msgn="";
    public ClienteInterface() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        chatMsgn = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        litaConectados = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        digitarMsgn = new javax.swing.JTextField();
        enviarMsgn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("chatPainel");

        chatMsgn.setEditable(false);
        chatMsgn.setColumns(20);
        chatMsgn.setRows(5);
        jScrollPane1.setViewportView(chatMsgn);

        jLabel1.setText("Conversa");

        jLabel2.setText("Digite");

        litaConectados.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel3.setText("Conectados");

        digitarMsgn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                digitarMsgnActionPerformed(evt);
            }
        });

        enviarMsgn.setText("Enviar");
        enviarMsgn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarMsgnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(digitarMsgn, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(enviarMsgn, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(1, 1, 1))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(litaConectados, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane1)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGap(267, 267, 267)))))
                                .addGap(63, 63, 63))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(litaConectados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(8, 8, 8)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(enviarMsgn)
                                                .addComponent(digitarMsgn)))
                                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>




    private void enviarMsgnActionPerformed(java.awt.event.ActionEvent evt) {
        String msgn = digitarMsgn.getText();
        saidaMsgn(msgn);
        digitarMsgn.setText("");
    }

    private void digitarMsgnActionPerformed(java.awt.event.ActionEvent evt) {
        enviarMsgnActionPerformed(evt);
    }

    public void iniciarChat()  {

        try {
            conexao = new Socket("localhost", 2000);

            saida = new PrintStream(conexao.getOutputStream());
            String meuNome = JOptionPane.showInputDialog(chatMsgn, "nome do usuario", "informacao", JOptionPane.PLAIN_MESSAGE);
            saida.println(meuNome);

            Receptor r = new Receptor(conexao.getInputStream());
            new Thread(r).start();
            executarChat();



        } catch (IOException ex) {
            System.out.println("nao foi possivel se conectar com o servidor");
        }

    }


    public void executarChat()   {

        try {

            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

            while (true){
                msgn = entrada.readLine();
                chatMsgn.append(msgn + "\n");

                if (msgn.trim().equals("")) {
                    System.out.println("Conexão encerrada!!!");
                    break;
                }


            }

        } catch (IOException ex) {
            System.out.println("nao foi possivel enviar mensagem");
        }
    }

    public void saidaMsgn(String msgn){
        try{
            saida.println(msgn);
        }catch (Exception e){
            chatMsgn.append("nao foi possivel enviar mensagem para o servidor");
        }
    }
    public static void main(String args[])  {

        ClienteInterface chat = new ClienteInterface();

        java.awt.EventQueue.invokeLater(() -> {
            new ClienteInterface().setVisible(true);
        });

        chat.iniciarChat();
    }



    // Variables declaration - do not modify
    static  javax.swing.JTextArea chatMsgn;
    static javax.swing.JComboBox<String> litaConectados;
    public javax.swing.JTextField digitarMsgn;
    public javax.swing.JButton enviarMsgn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration

    class Receptor implements Runnable {

        private InputStream servidor;

        public Receptor(InputStream servidor) {
            this.servidor = servidor;
        }

        @Override
        public void run() {
            
        }

    }
}
