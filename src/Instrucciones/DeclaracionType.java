/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Instrucciones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.Simbolo;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;
import java.util.ArrayList;

/**
 *
 * @author Pavel
 */
public class DeclaracionType implements Instruccion {

    private ArrayList<String> identificadores;
    private Tipo tipo;
    private int fila;
    private int columna;

    public DeclaracionType(Tipo tipo, ArrayList<String> identificadores, int fila, int columna) {
        this.tipo = tipo;
        this.identificadores = identificadores;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        for(int i = 0; i < identificadores.size(); i++){
            String identificador = identificadores.get(i);
            Simbolo simbolo = new Simbolo(identificador, tipo, tabla.getAmbito(), "variable", false, tabla.getHeap());
            Object result = tabla.InsertarVariable(simbolo);
            if (result != null) {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        (String) result,
                        fila, columna);
                arbol.getErrores().add(exc);
                return exc;
            }
        }
        return null;
    }

    @Override
    public String get4D() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
