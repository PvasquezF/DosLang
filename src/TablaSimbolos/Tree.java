/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Excepciones.Excepcion;
import Interfaces.AST;
import java.util.ArrayList;

/**
 *
 * @author Pavel
 */
public class Tree {
    private ArrayList<AST> instrucciones;
    private ArrayList<Excepcion> errores;
    public Tree(ArrayList<AST> instrucciones){
        this.instrucciones = instrucciones;
        this.errores = new ArrayList<>();
    }

    public ArrayList<AST> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<AST> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public ArrayList<Excepcion> getErrores() {
        return errores;
    }

    public void setErrores(ArrayList<Excepcion> errores) {
        this.errores = errores;
    }
}
