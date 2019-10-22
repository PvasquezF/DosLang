/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Interfaces.Expresion;

/**
 *
 * @author Pavel
 */

public class Dimension {
    private Expresion limiteInferior;
    private Expresion limiteSuperior;
    private int fila;
    private int columna;
    
    public Dimension(Expresion limiteInferior, Expresion limiteSuperior, int fila, int columna){
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.fila = fila;
        this.columna = columna;
    }

    public Expresion getLimiteInferior() {
        return limiteInferior;
    }

    public void setLimiteInferior(Expresion limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public Expresion getLimiteSuperior() {
        return limiteSuperior;
    }

    public void setLimiteSuperior(Expresion limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
}
