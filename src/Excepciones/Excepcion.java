/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excepciones;

import TablaSimbolos.Simbolo;
import TablaSimbolos.Tipo;

/**
 *
 * @author Pavel
 */
public class Excepcion {

    public static enum TIPOERROR {

        LEXICO,
        SINTACTICO,
        SEMANTICO
    }
    private final int linea;
    private final int columna;
    private final String descripcion;
    private final TIPOERROR tipoError;

    public Excepcion(TIPOERROR tipoError, String descripcion, int linea, int columna) {
        this.tipoError = tipoError;
        this.linea = linea;
        this.columna = columna;
        this.descripcion = descripcion;
    }

    public String ToString() {
        return this.tipoError + " " + this.descripcion + " " + this.linea + " " + this.columna;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TIPOERROR getTipoError() {
        return tipoError;
    }
}
