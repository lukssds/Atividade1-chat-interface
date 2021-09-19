package chat;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;


/*
    Atividade1
    Materia:Sistemas Distribuidos
    Professor:Ricardo Boaventura
    Alunos: Leonardo Borges, Igor Machado, Lucas Samuel
 */

public class Servidor extends Thread {

    private static Vector clientes;
    private Socket conexao;
    private String meuNome;
    private Integer numeroEnvio;
    private BufferedReader entrada;
    private PrintStream saida;

    private PrintWriter printWriter;
    private static FileWriter fileWriter;
    private Integer porta;
    private InetAddress enderecoIp;

    public Servidor(Socket s) {
        conexao = s;

        try {
            fileWriter = new FileWriter("logs.txt", true);
            printWriter = new PrintWriter(fileWriter);
            entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            saida = new PrintStream(conexao.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        clientes = new Vector();

        try {
            ServerSocket s = new ServerSocket(2000);

            while (true) {
                System.out.print("Esperando Conectar...");
                Socket conexao = s.accept();
                System.out.println("Conectou!");
                Thread t = new Servidor(conexao);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

            PrintStream saida = new PrintStream(conexao.getOutputStream());

            String log = "";
            porta = conexao.getPort();
            enderecoIp = conexao.getInetAddress();
            meuNome = entrada.readLine();

            if (meuNome == null) {
                return;
            }

            clientes.add(saida);
            String linha = entrada.readLine();

            while ((linha != null && (!linha.trim().equals("")))) {
                log = createLog(linha);
                printWriter.println(log);
                printWriter.flush();

                if (linha.contains("privada:")) {
                    sendToOne(saida, "disse: ", linha, Integer.parseInt(linha.split(":")[1].toString()));
                    linha = entrada.readLine();

                } else {
                    sendToAll(saida, " disse: ", linha);
                    linha = entrada.readLine();
                }

            }
            sendToAll(saida, " saiu ", " do Chat!");
            clientes.remove(saida);
            conexao.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String createLog(String linha) {
        String nome = "<" + meuNome + ">";
        String ip = "<" + enderecoIp.getHostAddress() + ">";

        String log = nome + "@" + ip + "@<" + porta + ">#<" + linha + ">";
        return log;
    }

    private void sendToAll(PrintStream saida, String acao, String linha) throws IOException {

        Enumeration e = clientes.elements();

        while (e.hasMoreElements()) {
            PrintStream chat = (PrintStream) e.nextElement();
            if (acao.equals(" saiu ")) {
                if (chat == saida) {
                    chat.println(meuNome + " Saiu do chat");
                }
            }
            chat.println(meuNome + acao + linha);
        }
    }

    private void sendToOne(PrintStream saida, String acao, String linha, int idEnvio) throws IOException {

        Object destinatario = clientes.get(idEnvio);
        PrintStream msgPrivada = (PrintStream) destinatario;

        msgPrivada.println(meuNome + " " + acao + "(privada) " + linha.split(":")[2]);

    }

}
