package Expresiones;

import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

import java.util.ArrayList;

public class AccesoArreglo implements Expresion {
    private ArrayList<Expresion> indices;
    public AccesoArreglo(ArrayList<Expresion> indices){
        this.indices = indices;
    }
    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        return new Tipo(Tipo.tipo.INTEGER, null);
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        return null;
    }

    public ArrayList<Expresion> getIndices() {
        return indices;
    }

    public void setIndices(ArrayList<Expresion> indices) {
        this.indices = indices;
    }
}
