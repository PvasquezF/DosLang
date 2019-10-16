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
import java.util.ArrayList;
import Excepciones.Excepcion;
import LexicoDosLang.Lexer;
import SintacticoDosLang.Syntax;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

/**
 *
 * @author Pavel
 */
public class DosLang extends Thread {

    /**
     * @param args the command line arguments //
     */
    public static ArrayList<Excepcion> errores;
    
    public static void main(String[] args) throws IOException, Exception {
        // TODO code application logic here
        errores = new ArrayList<>();
        //Lexer lexer = new Lexer(new BufferedReader(new StringReader(entrada2)));
        Lexer lexer = new Lexer(new BufferedReader(new FileReader("C:\\Users\\Pavel\\Desktop\\entradaDosLang.txt")));
        Syntax s = new Syntax(lexer);
        s.parse();
        errores.forEach(m->{System.err.println(m.ToString());});
        //Listener socket = new Listener();
        //socket.connect();
        //socket.start();

    }
}
