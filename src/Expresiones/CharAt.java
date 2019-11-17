package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

public class CharAt implements Expresion {

    private Expresion cadena;
    private Expresion posicion;
    private int fila;
    private int columna;

    public CharAt(Expresion cadena, Expresion posicion, int fila, int columna) {
        this.cadena = cadena;
        this.posicion = posicion;
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
            Excepcion e = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "El parametro 1 de la funcion charAt necesita un string.",
                    this.fila, this.columna);
            arbol.getErrores().add(e);
            return e;
        }

        result = posicion.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        tipo = (Tipo) result;
        if (tipo.getType() != Tipo.tipo.INTEGER && tipo.getType() != Tipo.tipo.CHAR) {
            Excepcion e = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "El parametro 2 de la funcion charAt necesita un integer.",
                    this.fila, this.columna);
            arbol.getErrores().add(e);
            return e;
        }
        return new Tipo(Tipo.tipo.CHAR);
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Inicio llamada chartAt fila: " + this.fila + ", columna: " + this.columna + "\n";
        codigo += this.cadena.get4D(tabla, arbol);
        String temp1 = tabla.getTemporalActual();
        codigo += this.posicion.get4D(tabla, arbol);
        String temp2 = tabla.getTemporalActual();
        codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "+,p,0," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + ",-1,stack\n";
        codigo += "+,p,1," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + "," + temp1 + ",stack\n";
        codigo += "+,p,2," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + "," + temp2 + ",stack\n";
        codigo += "call,,,CharAt_primitiva\n";
        codigo += "+,p,0," + tabla.getTemporal() + "\n";
        codigo += "=,stack," + tabla.getTemporalActual() + "," + tabla.getTemporal() + "\n";
        codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "// Fin llamada charAt\n";
        return codigo;
    }
}
