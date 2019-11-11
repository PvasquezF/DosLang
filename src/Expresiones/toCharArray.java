package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.Dimension;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

import java.util.ArrayList;

public class toCharArray implements Expresion {

    private Expresion valor;
    private int fila;
    private int columna;

    public toCharArray(Expresion valor, int fila, int columna) {
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        Object result = valor.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        Tipo tipo = (Tipo) result;
        if (tipo.getType() != Tipo.tipo.STRING) {
            Excepcion e = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "El parametro 1 de la funcion toCharArray necesita un string.",
                    this.fila, this.columna);
            arbol.getErrores().add(e);
            return e;
        }
        ArrayList<Dimension> d = new ArrayList<>();
        d.add(new Dimension(new Primitivo(0), new Primitivo(50), fila, columna));
        Tipo t = new Tipo(Tipo.tipo.ARREGLO, Tipo.tipo.CHAR, null, d);
        return t;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Inicio llamada toCharArray fila: " + this.fila + ", columna: " + this.columna + "\n";
        codigo += this.valor.get4D(tabla, arbol);
        String temp1 = tabla.getTemporalActual();
        codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "+,p,0," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + ",-1,stack\n";
        codigo += "+,p,1," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + "," + temp1 + ",stack\n";
        codigo += "call,,,toCharArrayPrimitiva\n";
        codigo += "+,p,0," + tabla.getTemporal() + "\n";
        codigo += "=,stack," + tabla.getTemporalActual() + "," + tabla.getTemporal() + "\n";
        codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "// Fin llamada toCharArray\n";
        return codigo;
    }
}