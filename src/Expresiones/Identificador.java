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
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

/**
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
        String codigo = "// Inicio identificador linea: " + fila + ", columna: " + columna + "\n";
        if (sim instanceof Simbolo) {
            accesoGlobal = ((Simbolo) sim).getNivel().equalsIgnoreCase("global");
            String temp1 = tabla.getTemporal();
            codigo += "=," + ((Simbolo) sim).getApuntador() + ",," + temp1 + "// Id = " + this.identificador + "\n";
            tabla.AgregarTemporal(temp1);
            if (asExpresion) {
                String temp2 = tabla.getTemporal();
                if (((Simbolo) sim).isReferencia() && (((Simbolo) sim).getTipo().getType() == Tipo.tipo.INTEGER
                        || ((Simbolo) sim).getTipo().getType() == Tipo.tipo.REAL
                        || ((Simbolo) sim).getTipo().getType() == Tipo.tipo.BOOLEAN
                        || ((Simbolo) sim).getTipo().getType() == Tipo.tipo.CHAR
                        || ((Simbolo) sim).getTipo().getType() == Tipo.tipo.WORD
                        || ((Simbolo) sim).getTipo().getType() == Tipo.tipo.STRING)) {
                    String label1 = tabla.getEtiqueta();
                    String label2 = tabla.getEtiqueta();
                    String temp4 = tabla.getTemporal();
                    String temp3 = tabla.getTemporal();
                    codigo += "+,p," + temp1 + "," + temp1 + "\n";
                    tabla.AgregarTemporal(temp1);

                    codigo += "+," + temp1 + ",1," + temp2 + "\n";
                    tabla.AgregarTemporal(temp2);
                    tabla.QuitarTemporal(temp1);

                    codigo += "=,stack," + temp2 + "," + temp4 + "\n";
                    tabla.AgregarTemporal(temp4);
                    tabla.QuitarTemporal(temp2);

                    codigo += "je," + temp4 + ",1," + label1 + "\n";
                    tabla.QuitarTemporal(temp4);

                    codigo += "=,stack," + temp1 + "," + temp3 + "\n";
                    tabla.AgregarTemporal(temp3);
                    tabla.QuitarTemporal(temp1);

                    codigo += "=,stack," + temp3 + "," + temp3 + "\n";
                    tabla.AgregarTemporal(temp3);

                    codigo += "jmp,,," + label2 + "\n";
                    codigo += label1 + ":\n";

                    codigo += "=,stack," + temp1 + "," + temp1 + "\n";
                    tabla.QuitarTemporal(temp1);
                    tabla.AgregarTemporal(temp1);

                    codigo += "=,heap," + temp1 + "," + temp3 + "\n";
                    tabla.AgregarTemporal(temp3);
                    tabla.QuitarTemporal(temp1);
                    codigo += label2 + ":\n";
                } else if (accesoGlobal) {
                    codigo += "=,heap," + temp1 + "," + temp2 + "\n";
                    tabla.AgregarTemporal(temp2);
                    tabla.QuitarTemporal(temp1);
                } else {
                    String temp3 = tabla.getTemporal();
                    codigo += "+,p," + temp1 + "," + temp2 + "\n";
                    tabla.AgregarTemporal(temp2);
                    tabla.QuitarTemporal(temp1);
                    codigo += "=,stack," + temp2 + "," + temp3 + "\n";
                    tabla.AgregarTemporal(temp3);
                    tabla.QuitarTemporal(temp2);
                }
            }

            codigo += "// Fin identificador\n";
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

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}
