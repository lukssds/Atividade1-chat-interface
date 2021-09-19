package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.JOptionPane;


/*
    Atividade1
    Materia:Sistemas Distribuidos
    Professor:Ricardo Boaventura
    Alunos: Leonardo Borges, Igor Machado, Lucas Samuel
*/


public class ClienteInterface extends javax.swing.JFrame {

    Socket conexao;

    private static BufferedReader entrada;
    private static PrintStream saida;
    private String msgn = "";

    public ClienteInterface() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        chatMsgn = new javax.swing.JTextArea();
        titulo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        digitarMsgn = new javax.swing.JTextField();
        enviarMsgn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("chatPainel");

        chatMsgn.setEditable(false);
        chatMsgn.setColumns(20);
        chatMsgn.setRows(5);
        jScrollPane1.setViewportView(chatMsgn);

        jLabel2.setText("Digite:");

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

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Para enviar mensgam privada digite: privada:index_do_array_do_destinatario. \n Exemplo: privada:1:olá! tudo bem ?");
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(digitarMsgn, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(enviarMsgn, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(267, 267, 267)))
                                .addGap(63, 63, 63))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(digitarMsgn)
                                        .addComponent(enviarMsgn))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
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
    public void iniciarChat() {

        try {
            conexao = new Socket("localhost", 2000);

            saida = new PrintStream(conexao.getOutputStream());
            String meuNome = JOptionPane.showInputDialog(chatMsgn, "nome do usuario", "informacao", JOptionPane.PLAIN_MESSAGE);
            titulo.setText("Seja bem vindo! " + meuNome);
            saida.println(meuNome);

            Receptor r = new Receptor(conexao.getInputStream());
            new Thread(r).start();

            executarChat();

        } catch (IOException ex) {
            System.out.println("nao foi possivel se conectar com o servidor");
        }

    }

    public void executarChat() {
        try {

            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

            while (true) {
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

    public void saidaMsgn(String msgn) {
        try {
            saida.println(msgn);
        } catch (Exception e) {
            chatMsgn.append("nao foi possivel enviar mensagem para o servidor");
        }
    }

    public static void main(String args[]) throws Exception {

        ClienteInterface chat = new ClienteInterface();
        java.awt.EventQueue.invokeLater(() -> {
            new ClienteInterface().setVisible(true);
        });

        chat.iniciarChat();

    }


    // Variables declaration - do not modify
    static javax.swing.JTextArea chatMsgn;
    public javax.swing.JTextField digitarMsgn;
    public javax.swing.JButton enviarMsgn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    static javax.swing.JLabel titulo;
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
