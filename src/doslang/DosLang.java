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
        String entrada = ""
                + "type prueba1, prueba2 = integer;"
                + "prueba1, prueba2 = integer;"
                + "prueba1, prueba2 = char;"
                + "prueba1, prueba2 = real;"
                + "prueba1, prueba2 = boolean;"
                + "prueba1, prueba2 = word;"
                + "prueba1, prueba2 = string;"
                + "prueba1, prueba2 = registros;"
                + "prueba1, prueba2 = array[3..5] of array[3..5] of array[3..5] of integer;";
        String entrada2 = "program prueba; uses a1, a2;";
        //Lexer lexer = new Lexer(new BufferedReader(new StringReader(entrada2)));
        Lexer lexer = new Lexer(new BufferedReader(new FileReader("C:\\Users\\Pavel\\Desktop\\entradaDosLang.txt")));
        Syntax s = new Syntax(lexer);
        s.parse();
        errores.forEach(m->{System.out.println(m.ToString());});
        //Listener socket = new Listener();
        //socket.connect();
        //socket.start();
    }
}
