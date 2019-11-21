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
import Instrucciones.*;
import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import LexicoDosLang.Lexer;
import SintacticoDosLang.Syntax;
import TablaSimbolos.*;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Collections;

/**
 * @author Pavel
 */
public class DosLang extends Thread {

    /**
     * @param args the command line arguments //
     */

    public static void main(String[] args){
        // TODO code application logic here
        Listener socket = new Listener();
        socket.connect();
        socket.start();
    }
}
