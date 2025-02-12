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
public class Nil implements Expresion {

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        return new Tipo(tipo.NIL);
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Inicio nil linea\n";
        codigo += "=, -1,, " + tabla.getTemporal()+"\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        codigo += " // Fin nil\n";
        return codigo;
    }

}
