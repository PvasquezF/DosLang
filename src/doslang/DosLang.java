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
import Instrucciones.Asignacion;
import Instrucciones.DeclaracionConstante;
import Instrucciones.DeclaracionType;
import Instrucciones.DeclaracionVar;
import Instrucciones.Program;
import Interfaces.Instruccion;
import LexicoDosLang.Lexer;
import SintacticoDosLang.Syntax;
import TablaSimbolos.GenerarNativas4D;
import TablaSimbolos.ReservarMemoria;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Collections;

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
        int espaciosReservaHeap = 0;
        for (int i = 0; i < t.getInstrucciones().size(); i++) {
            Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
            if (ins instanceof DeclaracionConstante || ins instanceof DeclaracionVar) {
                espaciosReservaHeap = ins.getEspacios(espaciosReservaHeap);
            } else if (ins instanceof DeclaracionType) {
                ins.ejecutar(tabla, t);
                espaciosReservaHeap = ins.getEspacios(espaciosReservaHeap);
            } else if (ins instanceof Program) {
                ins.ejecutar(tabla, t);
            }
        }
        for (int i = 0; i < t.getInstrucciones().size(); i++) {
            Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
            if (ins instanceof DeclaracionConstante
                    || ins instanceof DeclaracionVar) {
                ins.ejecutar(tabla, t);
            }
        }
        Cuadruplos += ReservarMemoria.Reservar(tabla, espaciosReservaHeap);
        for (int i = 0; i < t.getInstrucciones().size(); i++) {
            Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
            if (ins instanceof DeclaracionConstante
                    || ins instanceof DeclaracionVar
                    || ins instanceof DeclaracionType) {
                Cuadruplos += ins.get4D(tabla, t);
            }
        }
        GenerarNativas4D gn4D = new GenerarNativas4D();
        Cuadruplos += gn4D.generarConcatenacion(tabla);
        Cuadruplos += gn4D.generarPrint(tabla);
        Cuadruplos += gn4D.generarTrunk(tabla);
        Cuadruplos += gn4D.generarRound(tabla);
        Cuadruplos += gn4D.generarChartAt(tabla);
        Cuadruplos += gn4D.generarLenght(tabla);
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
