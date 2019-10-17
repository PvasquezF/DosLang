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
import Instrucciones.DeclaracionConstante;
import Instrucciones.DeclaracionType;
import Instrucciones.DeclaracionVar;
import Interfaces.Instruccion;
import LexicoDosLang.Lexer;
import SintacticoDosLang.Syntax;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;
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
        //Lexer lexer = new Lexer(new BufferedReader(new FileReader("C:\\Users\\Pavel\\Desktop\\entradaDosLang.txt")));
        Lexer lexer = new Lexer(new BufferedReader(new FileReader("C:\\Users\\Pavel\\Desktop\\entradaDosLang2.txt")));
        Syntax s = new Syntax(lexer);
        s.parse();
        Tree t = s.getArbol();
        Tabla tabla = new Tabla();
        String Cuadruplos = "";
        for (int i = 0; i < t.getInstrucciones().size(); i++) {
            Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
            ins.ejecutar(tabla, t);
        }
        for (int i = 0; i < t.getInstrucciones().size(); i++) {
            Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
            if (ins instanceof DeclaracionConstante || ins instanceof DeclaracionVar) {
                Cuadruplos += ins.get4D(tabla, t);
            }
        }
        System.out.println(Cuadruplos);
        tabla.generarTablaHTML();
        errores.addAll(t.getErrores());
        errores.forEach(m -> {
            System.err.println(m.ToString());
        });
        //Listener socket = new Listener();
        //socket.connect();
        //socket.start();

    }
}
