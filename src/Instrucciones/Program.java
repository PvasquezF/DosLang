/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Instrucciones;

import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;

/**
 * @author Pavel
 */
public class Program implements Instruccion {

    private String nombreProgram;

    public Program(String nombreProgram) {
        this.nombreProgram = nombreProgram;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        //Siempre estara en el top de la pila para determinar el ambito actual,
        //Si se llama otro program, este pasara a ser el primero, y al terminar 
        //se hara pop y el anterior volvera a ser el ambito actual
        tabla.getListaAmbitos().add(nombreProgram.toLowerCase());
        tabla.setAmbito(nombreProgram.toLowerCase());
        return null;
    }

    @Override
    public String get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        return codigo;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }
}
