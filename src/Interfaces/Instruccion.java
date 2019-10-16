/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;

/**
 *
 * @author Pavel
 */
public interface Instruccion extends AST {
    Object ejecutar(Tabla tabla, Tree arbol);
}
