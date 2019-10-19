/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Instrucciones;

import Expresiones.Identificador;
import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;
import java.util.ArrayList;

/**
 *
 * @author Pavel
 */
public class Variable implements Expresion {

    private ArrayList<Expresion> listaExpresiones;
    public boolean accesoGlobal = false;
    public Variable(ArrayList<Expresion> listaExpresiones) {
        this.listaExpresiones = listaExpresiones;
    }
    
    public Variable(Expresion exp) {
        this.listaExpresiones = new ArrayList<>();
        this.listaExpresiones.add(exp);
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        return listaExpresiones.get(0);
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        Identificador id = (Identificador)this.listaExpresiones.get(0);
        id.getTipo(tabla, arbol);
        accesoGlobal = id.accesoGlobal;
        return this.listaExpresiones.get(0).get4D(tabla, arbol);
    }

}
