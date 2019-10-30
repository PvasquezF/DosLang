package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.*;

import java.util.ArrayList;

public class Llamada implements Expresion {
    private String nombre;
    private String nombreFuncion;
    private ArrayList<Expresion> parametros;
    private int fila;
    private int columna;

    public Llamada(String nombre, ArrayList<Expresion> parametros, int fila, int columna) {
        this.nombre = nombre;
        this.parametros = parametros;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        String nombreCompleto = "";
        Object respuestaNombre = generarNombreCompleto(tabla, arbol);
        if (respuestaNombre instanceof Excepcion) {
            return respuestaNombre;
        }
        nombreCompleto = (String) respuestaNombre;
        this.nombreFuncion = nombreCompleto;
        Object result = tabla.getFuncion(nombreCompleto);
        if (result instanceof String) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    (String) result,
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        Simbolo sim = (Simbolo) result;
        return sim.getTipo();
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        return null;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        Object result = tabla.getFuncion(this.nombreFuncion);
        if (result instanceof String) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    (String) result,
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        Simbolo sim = (Simbolo) result;
        String temp3 = tabla.getTemporal();
        for (int i = 0; i < parametros.size(); i++) {
            int contador = i + 1;
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            Parametro param = sim.getParametros().get(i);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + "," + temp1 + "\n";
            codigo += "+," + temp1 + "," + contador + "," + temp2 + "\n";
            /*if (param.isReferencia()) {
                if (param.getTipo().getType() == Tipo.tipo.INTEGER) {
                    codigo += parametros.get(i).get4D(tabla, arbol);
                }
            } else { // por valor
                if (param.getTipo().getType() == Tipo.tipo.INTEGER) {
                }
            }*/
            codigo += parametros.get(i).get4D(tabla, arbol);
            codigo += "=," + temp2 + "," + tabla.getTemporalActual() + ",stack\n";
        }
        codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p // Cambio de ambito\n";
        codigo += "call,,," + sim.getNombreCompleto() + "\n";
        codigo += "+,p,0," + temp3 + "// Retorno\n";
        codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p // Cambio de ambito\n";
        codigo += "=,stack," + temp3 + "," + tabla.getTemporal() + "\n";
        return codigo;
    }

    public Object generarNombreCompleto(Tabla tabla, Tree arbol) {
        String nombreCompleto = this.nombre;
        for (int i = 0; i < this.parametros.size(); i++) {
            Expresion exp = this.parametros.get(i);
            Object result = exp.getTipo(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Tipo t = (Tipo) result;
            if (t.getType() == Tipo.tipo.ARREGLO) {
                nombreCompleto += "_" + t.getType() + "_" + t.getTipoArreglo();
                if (t.getTipoArreglo() == Tipo.tipo.RECORD) {
                    nombreCompleto += "_" + t.getTipoObjeto();
                }
            } else if (t.getType() == Tipo.tipo.RECORD) {
                nombreCompleto += "_" + t.getType() + "_" + t.getTipoObjeto();
            } else {
                nombreCompleto += "_" + t.getType();
            }
        }
        return nombreCompleto;
    }
}
