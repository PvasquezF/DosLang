/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Instrucciones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.Simbolo;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;
import java.util.ArrayList;

/**
 *
 * @author Pavel
 */
public class DeclaracionConstante implements Instruccion {

    private ArrayList<String> identificadores;
    private Tipo tipo;
    Expresion valor;
    private int fila;
    private int columna;

    public DeclaracionConstante(Tipo tipo, ArrayList<String> identificadores, Expresion valor, int fila, int columna) {
        this.tipo = tipo;
        this.identificadores = identificadores;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        for (int i = 0; i < identificadores.size(); i++) {
            String identificador = identificadores.get(i);
            Simbolo simbolo = new Simbolo(identificador, tipo, tabla.getAmbito(), "variable", "global", valor, true, tabla.getStack());
            Object resultTipo = valor.getTipo(tabla, arbol);
            if (resultTipo instanceof Excepcion) {
                return resultTipo;
            }
            if ((tipo.equals(new Tipo(Tipo.tipo.CHAR))
                    || tipo.equals(new Tipo(Tipo.tipo.INTEGER))
                    || tipo.equals(new Tipo(Tipo.tipo.REAL)))
                    && ((Tipo) resultTipo).equals(new Tipo(Tipo.tipo.NIL))) {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        "No se puede asignar NIL al tipo " + tipo.toString() + ".",
                        fila, columna);
                arbol.getErrores().add(exc);
                return exc;
            }
            Tipo tipoValor = (Tipo) resultTipo;
            if (tipo.equals(tipoValor)) {
                Object result = tabla.InsertarVariable(simbolo);
                if (result != null) {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            (String) result,
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
            } else {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        "El tipo de la variable no coincide con el valor a asignar.",
                        fila, columna);
                arbol.getErrores().add(exc);
                return exc;
            }
        }
        return null;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        for (int i = 0; i < identificadores.size(); i++) {
            String identificador = identificadores.get(i);
            Object result = tabla.getVariable(identificador);
            if (result instanceof String) {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        (String) result,
                        fila, columna);
                arbol.getErrores().add(exc);
                return exc;
            } else {
                String temp1 = tabla.getTemporal();
                Simbolo sim = (Simbolo) result;
                codigo += "=," + sim.getApuntador() + ",," + temp1+"\n";
                codigo += sim.getValor().get4D(tabla, arbol);
                codigo += "=, " + temp1 + ", " + tabla.getTemporalActual() + ", stack\n";
            }
        }
        return codigo;
    }

}
