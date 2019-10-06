package Conexion;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Listener extends Thread {

    private String host = "localhost";
    private int port = 3001;
    volatile boolean run = false;
    volatile boolean halt = false;
    private boolean leyendo = false;
    Socket socket = null;
    BufferedReader stream = null;
    ServerSocket server;
    Socket client;
    InputStream input;

    /**
     * When an object implementing interface <code>Runnable</code> is used to
     * create a thread, starting the thread causes the object's <code>run</code>
     * method to be called in that separately executing thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may take
     * any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (run) {
            while (halt) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                server = new ServerSocket(3002);
                client = server.accept();
                input = client.getInputStream();
                String inputString = inputStreamAsString(input);
                System.out.println(inputString);
                //************RESPUESTA
                Socket tcpClient = new Socket("localhost", 3001);
                String msg = "hola desde java";
                DataOutputStream os = new DataOutputStream(tcpClient.getOutputStream());
                PrintWriter pw = new PrintWriter(os);
                pw.println(msg);
                pw.flush();
                //************
                client.close();
                server.close();
                System.out.println("********************");
            } catch (IOException ex) {
                Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void connect() {
//        try {
        //this.socket = new Socket(host, port);
        //this.stream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        run = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    synchronized void pause() {
        this.halt = true;
    }

    synchronized void unpause() {
        this.halt = false;
        notifyAll();
    }

    public String inputStreamAsString(InputStream stream) throws IOException {
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
