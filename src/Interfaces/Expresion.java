/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

/**
 *
 * @author Pavel
 */
public interface Expresion extends AST {
    Tipo getTipo();
    Object getValor(Tabla tabla, Tree arbol);
}
