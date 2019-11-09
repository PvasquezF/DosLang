package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

public class Reemplazar implements Expresion {
    private Expresion cadena;
    private Expresion cadena2;
    private int fila;
    private int columna;

    public Reemplazar(Expresion cadena, Expresion cadena2, int fila, int columna) {
        this.cadena = cadena;
        this.cadena2 = cadena2;
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
            Excepcion e = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "El parametro 1 de la funcion replace necesita un string.",
                    this.fila, this.columna);
            arbol.getErrores().add(e);
            return e;
        }

        result = cadena2.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        tipo = (Tipo) result;
        if (tipo.getType() != Tipo.tipo.STRING) {
            Excepcion e = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "El parametro 2 de la funcion replace necesita un string.",
                    this.fila, this.columna);
            arbol.getErrores().add(e);
            return e;
        }

        return new Tipo(Tipo.tipo.STRING);
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        return null;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Inicio llamada replace fila: " + this.fila + ", columna: " + this.columna + "\n";
        codigo += this.cadena.get4D(tabla, arbol);
        String temp1 = tabla.getTemporalActual();
        codigo += this.cadena2.get4D(tabla, arbol);
        String temp2 = tabla.getTemporalActual();
        codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "+,p,0," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + ",-1,stack\n";

        codigo += "+,p,1," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + "," + temp1 + ",stack\n";

        codigo += "+,p,2," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + "," + temp2 + ",stack\n";

        codigo += "call,,,reemplazar_primitiva\n";
        codigo += "+,p,0," + tabla.getTemporal() + "\n";
        codigo += "=,stack," + tabla.getTemporalActual() + "," + tabla.getTemporal() + "\n";
        codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "// Fin llamada replace\n";
        return codigo;
    }
}
