/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doslang;

import Conexion.Listener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Pavel
 */
public class DosLang extends Thread  {

    /**
     * @param args the command line arguments //
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
<<<<<<< Updated upstream
        Listener socket = new Listener();
        socket.connect();
        socket.start();
//        Socket tcpClient = new Socket("localhost", 3001);
//        String msg = "pavel";
//        DataOutputStream os = new DataOutputStream(tcpClient.getOutputStream());
//        PrintWriter pw = new PrintWriter(os);
//        pw.println(msg);
//        pw.flush();
=======
        errores = new ArrayList<>();
        //Lexer lexer = new Lexer(new BufferedReader(new StringReader(entrada2)));
        Lexer lexer = new Lexer(new BufferedReader(new FileReader("C:\\Users\\Pavel\\Desktop\\entradaDosLang.txt")));
        Syntax s = new Syntax(lexer);
        s.parse();
        errores.forEach(m->{System.err.println(m.ToString());});
//        Listener socket = new Listener();
//        socket.connect();
//        socket.start();
>>>>>>> Stashed changes
    }
//    public static void main(String[] args) {
//        ServerSocket server;
//        Socket client;
//        InputStream input;
//
//        try {
//            server = new ServerSocket(3002);
//            client = server.accept();
//            input = client.getInputStream();
//            String inputString = inputStreamAsString(input);
//
//            System.out.println(inputString);
//
//            //************RESPUESTA
//            Socket tcpClient = new Socket("localhost", 3001);
//            String msg = "hola desde java";
//            DataOutputStream os = new DataOutputStream(tcpClient.getOutputStream());
//            PrintWriter pw = new PrintWriter(os);
//            pw.println(msg);
//            pw.flush();
//            //************
//
//            //client.close();
//            //server.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static String inputStreamAsString(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        return sb.toString();
    }

}
