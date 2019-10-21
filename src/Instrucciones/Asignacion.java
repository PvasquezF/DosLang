/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Instrucciones;

import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;

/**
 *
 * @author Pavel
 */
public class Asignacion implements Instruccion {

    Variable variable;
    Expresion valor;
    int fila;
    int columna;

    public Asignacion(Variable variable, Expresion valor, int fila, int columna) {
        this.variable = variable;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        codigo += variable.get4D(tabla, arbol);
        String temp1 = tabla.getTemporalActual();
        if (variable.accesoGlobal) {     
            codigo += valor.get4D(tabla, arbol);
            codigo += "=," + temp1 + "," + tabla.getTemporalActual() + ",heap\n";
        }
        return codigo;
    }

    @Override
    public int getEspacios(int espacios) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
