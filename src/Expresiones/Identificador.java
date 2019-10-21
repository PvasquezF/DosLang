/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.Simbolo;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;

/**
 *
 * @author Pavel
 */
public class Identificador implements Expresion {

    private String identificador;
    public boolean accesoGlobal = false;
    public boolean asExpresion = false;
    private int fila;
    private int columna;

    public Identificador(String identificador, int fila, int columna) {
        this.identificador = identificador;
        this.fila = fila;
        this.columna = columna;
    }

    public Identificador(String identificador, boolean asExpresion, int fila, int columna) {
        this.identificador = identificador;
        this.asExpresion = asExpresion;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        Object sim = tabla.getVariable(identificador);
        if (sim instanceof Simbolo) {
            accesoGlobal = ((Simbolo) sim).getNivel().equalsIgnoreCase("global");
            return ((Simbolo) sim).getTipo();
        } else {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    sim.toString(),
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        Object sim = tabla.getVariable(identificador);
        if (sim instanceof Simbolo) {
            accesoGlobal = ((Simbolo) sim).getNivel().equalsIgnoreCase("global");
            return sim;
        } else {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    sim.toString(),
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        Object sim = tabla.getVariable(identificador);
        String codigo = "";
        if (sim instanceof Simbolo) {
            accesoGlobal = ((Simbolo) sim).getNivel().equalsIgnoreCase("global");
            String temp1 = tabla.getTemporal();
            codigo += "=," + ((Simbolo) sim).getApuntador() + ",," + temp1 + "// Id = " + this.identificador + "\n";
            if (asExpresion) {
                String temp2 = tabla.getTemporal();
                if (accesoGlobal) {
                    codigo += "=,heap," + temp1 + "," + temp2 + "\n";
                } else {
                    codigo += "=,stack," + temp1 + "," + temp2 + "\n";
                }
            }
            return codigo;
        } else {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    sim.toString(),
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
    }

    public String getIdentificador() {
        return identificador;
    }

}
