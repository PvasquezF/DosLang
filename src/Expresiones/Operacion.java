/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Expresiones;

import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tipo.tipo;
import TablaSimbolos.Tree;

/**
 *
 * @author Pavel
 */
public class Operacion implements Expresion{
    public enum Operador {
        SUMA,
        RESTA,
        MULTIPLICACION,
        DIVISION,
        POTENCIA,
        MENOS_UNARIO,
        AUMENTO,
        DECREMENTO,
        MAYOR_QUE,
        MENOR_QUE,
        MAYOR_IGUAL,
        MENOR_IGUAL,
        IGUAL_IGUAL,
        DIFERENTE_QUE,
        OR,
        AND,
        NOT,
        NULO
    }

    public static Operador getOperador(String op) {
        switch (op) {
            case "+":
                return Operador.SUMA;
            case "-":
                return Operador.RESTA;
            case "*":
                return Operador.MULTIPLICACION;
            case "/":
                return Operador.DIVISION;
            case "^":
                return Operador.POTENCIA;
            case "++":
                return Operador.AUMENTO;
            case "--":
                return Operador.DECREMENTO;
            case ">":
                return Operador.MAYOR_QUE;
            case "<":
                return Operador.MENOR_QUE;
            case "==":
                return Operador.IGUAL_IGUAL;
            case "!=":
                return Operador.DIFERENTE_QUE;
            case "||":
                return Operador.OR;
            case "&&":
                return Operador.AND;
            default:
                return Operador.NULO;
        }
    }

    private Expresion operando1;

    private Expresion operando2;

    private Expresion operandoU;

    private Operador operador;
    int fila;
    int columna;
    @Override
    public String get4D() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tipo getTipo() {
        return new Tipo(tipo.BOOLEAN, "");
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
