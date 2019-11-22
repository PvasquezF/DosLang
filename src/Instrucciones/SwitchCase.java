package Instrucciones;

import Excepciones.Excepcion;
import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;

import java.util.ArrayList;

public class SwitchCase implements Instruccion {
    private Expresion condicion;
    private ArrayList<Case> casos;
    private ArrayList<AST> defecto;
    private int fila;
    private int columna;

    public SwitchCase(Expresion condicion, ArrayList<Case> casos, ArrayList<AST> defecto, int fila, int columna) {
        this.condicion = condicion;
        this.casos = casos;
        this.defecto = defecto;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        Object result = condicion.get4D(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }

        for (Case caso : casos) {
            for (Expresion exp : caso.getCondiciones()) {
                result = exp.getTipo(tabla, arbol);
                if (result instanceof Excepcion) {
                    return result;
                }
            }
            for (AST ast : caso.getInstrucciones()) {
                if (ast instanceof Instruccion) {
                    result = ((Instruccion) ast).ejecutar(tabla, arbol);
                } else {
                    result = ((Expresion) ast).getTipo(tabla, arbol);
                }
                if (result instanceof Excepcion) {
                    return result;
                }
            }
        }

        for (AST ast : defecto) {
            if (ast instanceof Instruccion) {
                result = ((Instruccion) ast).ejecutar(tabla, arbol);
            } else {
                result = ((Expresion) ast).getTipo(tabla, arbol);
            }
            if (result instanceof Excepcion) {
                return result;
            }
        }
        return null;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        String temp1 = tabla.getTemporal();
        codigo += condicion.get4D(tabla, arbol);
        codigo += "=," + tabla.getTemporalActual() + ",," + temp1 + " // Condicion switch\n";
        tabla.AgregarTemporal(temp1);
        tabla.QuitarTemporal(tabla.getTemporalActual());
        ArrayList<String> tagsFinal = new ArrayList<>();
        for (Case caso : casos) {
            ArrayList<String> tagsTrue = new ArrayList<>();
            ArrayList<String> tagsFalse = new ArrayList<>();
            for (Expresion exp : caso.getCondiciones()) {
                String label1 = tabla.getEtiqueta();
                String label2 = tabla.getEtiqueta();
                String temp2 = tabla.getTemporal();
                codigo += exp.get4D(tabla, arbol);
                codigo += "=," + tabla.getTemporalActual() + ",," + temp2 + "\n";
                tabla.AgregarTemporal(temp2);
                tabla.QuitarTemporal(tabla.getTemporalActual());
                codigo += "je," + temp1 + "," + temp2 + "," + label1 + "\n";
                tabla.QuitarTemporal(temp1);
                tabla.QuitarTemporal(temp2);
                tagsTrue.add(label1);
                tagsFalse.add(label2);
            }
            for (String s : tagsFalse) {
                codigo += "jmp,,," + s + "\n";
            }
            for (String s : tagsTrue) {
                codigo += s + ":\n";
            }
            for (AST ast : caso.getInstrucciones()) {
                codigo += ast.get4D(tabla, arbol);
            }
            codigo += "jmp,,," + tabla.getEtiqueta() + "\n";
            tagsFinal.add(tabla.getEtiquetaActual());
            for (String s : tagsFalse) {
                codigo += s + ":\n";
            }
        }
        for (AST ast : defecto) {
            codigo += ast.get4D(tabla, arbol);
        }
        for (String s : tagsFinal) {
            codigo += s + ":\n";
        }
        return codigo;
    }
}
