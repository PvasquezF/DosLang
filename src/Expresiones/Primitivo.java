/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Expresiones;

import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tipo.tipo;
import TablaSimbolos.Tree;

/**
 *
 * @author Pavel
 */
public class Primitivo implements Expresion {

    private Object valor;

    public Primitivo(Object valor) {
        this.valor = valor;
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        if (valor instanceof String) {
            return new Tipo(tipo.STRING);
        } else if (valor instanceof Integer) {
            return new Tipo(tipo.INTEGER);
        } else if (valor instanceof Character) {
            return new Tipo(tipo.CHAR);
        } else if (valor instanceof Double) {
            return new Tipo(tipo.REAL);
        } else if (valor instanceof Boolean) {
            return new Tipo(tipo.BOOLEAN);
        } else if (valor instanceof Nil) {
            return new Tipo(tipo.NIL);
        } else {
            return new Tipo(tipo.NIL);
        }
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String get4D() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
