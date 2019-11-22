package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

public class Redondear implements Expresion {

    private Expresion valor;
    private int fila;
    private int columna;

    public Redondear(Expresion valor, int fila, int columna) {
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
        if (tipo.getType() != Tipo.tipo.REAL) {
            Excepcion e = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "El parametro 1 de la funcion round necesita un real.",
                    this.fila, this.columna);
            arbol.getErrores().add(e);
            return e;
        }

        return new Tipo(Tipo.tipo.INTEGER);
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Inicio llamada round fila: " + this.fila + ", columna: " + this.columna + "\n";
        codigo += this.valor.get4D(tabla, arbol);
        String temp1 = tabla.getTemporalActual();
        codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "+,p,0," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + ",-1,stack\n";
        codigo += "+,p,1," + tabla.getTemporal() + "\n";
        codigo += "=," + tabla.getTemporalActual() + "," + temp1 + ",stack\n";
        codigo += "call,,,round_primitiva\n";
        codigo += "+,p,0," + tabla.getTemporal() + "\n";
        codigo += "=,stack," + tabla.getTemporalActual() + "," + tabla.getTemporal() + "\n";
        codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "// Fin llamada round\n";
        return codigo;
    }
}