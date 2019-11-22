package Instrucciones;

import Interfaces.AST;
import Interfaces.Expresion;

import java.util.ArrayList;

public class Case {
    private ArrayList<Expresion> condiciones;
    private ArrayList<AST> instrucciones;

    public Case(ArrayList<Expresion> condiciones, ArrayList<AST> instrucciones) {
        this.condiciones = condiciones;
        this.instrucciones = instrucciones;
    }

    public ArrayList<Expresion> getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(ArrayList<Expresion> condiciones) {
        this.condiciones = condiciones;
    }

    public ArrayList<AST> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<AST> instrucciones) {
        this.instrucciones = instrucciones;
    }
}
