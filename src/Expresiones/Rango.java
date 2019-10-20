/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Expresiones;

import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

/**
 *
 * @author Pavel
 */
public class Rango implements Expresion {

    private Expresion lowerRange;
    private Expresion upperRange;
    private Tipo tipo;
    private int fila;
    private int columna;

    public Rango(Expresion lowerRange, Expresion upperRange, Tipo tipo, int fila, int columna) {
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        return this.tipo;
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        return null;
    }

}
