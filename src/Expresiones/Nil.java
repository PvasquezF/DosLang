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
public class Nil implements Expresion{

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        return new Tipo(tipo.NIL);
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
