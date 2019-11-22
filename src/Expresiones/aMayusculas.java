package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

public class aMayusculas implements Expresion {

    private Expresion cadena;
    private Expresion posicion;
    private int fila;
    private int columna;

    public aMayusculas(Expresion cadena, int fila, int columna) {
        this.cadena = cadena;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        Object result = cadena.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        Tipo tipo = (Tipo) result;
        if (tipo.getType() != Tipo.tipo.STRING) {
            Excepcion e = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "El parametro 1 de la funcion toUpper necesita un string.",
                    this.fila, this.columna);
            arbol.getErrores().add(e);
            return e;
        }
        return new Tipo(Tipo.tipo.STRING);
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Inicio llamada toUpper fila: " + this.fila + ", columna: " + this.columna + "\n";
        codigo += this.cadena.get4D(tabla, arbol);
        String temp1 = tabla.getTemporalActual();
        codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "+,p,0," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + ",-1,stack\n";
        codigo += "+,p,1," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + "," + temp1 + ",stack\n";
        codigo += "call,,,toUpperPrimitiva\n";
        codigo += "+,p,0," + tabla.getTemporal() + "\n";
        codigo += "=,stack," + tabla.getTemporalActual() + "," + tabla.getTemporal() + "\n";
        codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "// Fin llamada toUpper\n";
        return codigo;
    }
}