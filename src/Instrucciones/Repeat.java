package Instrucciones;

import Excepciones.Excepcion;
import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;

import java.util.ArrayList;

public class Repeat implements Instruccion {
    private Expresion condicion;
    private ArrayList<AST> instrucciones;
    private int fila;
    private int columna;

    public Repeat(Expresion condicion, ArrayList<AST> instrucciones, int fila, int columna) {
        this.condicion = condicion;
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        Object result = this.condicion.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }

        for (int i = 0; i < this.instrucciones.size(); i++) {
            AST ast = this.instrucciones.get(i);
            if (ast instanceof Instruccion) {
                result = ((Instruccion) ast).ejecutar(tabla, arbol);
            } else {
                result = ((Expresion) ast).getTipo(tabla, arbol);
            }
            if (result instanceof Excepcion) {
                return result;
            }
        }
        tabla.getSentenciasBreakActivas().clear();
        tabla.getSentenciasContinueActivas().clear();
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
        String label1 = tabla.getEtiqueta();
        codigo += label1 + ":\n";
        for (int i = 0; i < this.instrucciones.size(); i++) {
            codigo += this.instrucciones.get(i).get4D(tabla, arbol);
        }
        for (Object o : tabla.getEtiquetasContinue()) {
            codigo += (String) o + ": // Continue\n";
        }
        codigo += this.condicion.get4D(tabla, arbol);
        codigo += "=," + tabla.getTemporalActual() + ",," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        tabla.QuitarTemporal(tabla.getTemporalActual());
        codigo += "je," + temp1 + ",1," + label1 + "\n";
        tabla.QuitarTemporal(temp1);
        for (Object o : tabla.getEtiquetasBreak()) {
            codigo += (String) o + ": // Break\n";
        }
        tabla.getEtiquetasBreak().clear();
        tabla.getEtiquetasContinue().clear();
        return codigo;
    }
}
