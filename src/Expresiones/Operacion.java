/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tipo.tipo;
import TablaSimbolos.Tree;

/**
 *
 * @author Pavel
 */
public class Operacion implements Expresion {

    public enum Operador {

        SUMA,
        RESTA,
        MULTIPLICACION,
        DIVISION,
        MODULO,
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
        NAND,
        NOR,
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
            case "%":
                return Operador.MODULO;
            case ">":
                return Operador.MAYOR_QUE;
            case "<":
                return Operador.MENOR_QUE;
            case ">=":
                return Operador.MAYOR_IGUAL;
            case "<=":
                return Operador.MENOR_IGUAL;
            case "=":
                return Operador.IGUAL_IGUAL;
            case "<>":
                return Operador.DIFERENTE_QUE;
            case "or":
                return Operador.OR;
            case "and":
                return Operador.AND;
            case "nand":
                return Operador.NAND;
            case "nor":
                return Operador.NOR;
            case "not":
                return Operador.NOT;
            default:
                return Operador.NULO;
        }
    }

    private Expresion operando1;
    private Expresion operando2;
    private Expresion operandoU;
    private Operador operador;
    private int fila;
    private int columna;

    public Operacion(Expresion operando1, Expresion operando2, Operador operador, int fila, int columna) {
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operador = operador;
        this.fila = fila;
        this.columna = columna;
    }

    public Operacion(Expresion operandoU, Operador operador, int fila, int columna) {
        this.operandoU = operandoU;
        this.operador = operador;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public String get4D() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        Object tipo1 = null, tipo2 = null, tipoU = null;
        Object tipoResultante = null;
        if (operandoU == null) {
            tipo1 = operando1.getTipo(tabla, arbol);
            if (tipo1 instanceof Excepcion) {
                return tipo1;
            } else if (!(tipo1 instanceof Tipo)) {
                tipoResultante = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Ha habido un error al realizar la operacion, op1", fila, columna);
                arbol.getErrores().add((Excepcion) tipoResultante);
                return tipoResultante;
            }
            tipo2 = operando2.getTipo(tabla, arbol);
            if (tipo2 instanceof Excepcion) {
                return tipo2;
            } else if (!(tipo2 instanceof Tipo)) {
                tipoResultante = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Ha habido un error al realizar la operacion, op2", fila, columna);
                arbol.getErrores().add((Excepcion) tipoResultante);
                return tipoResultante;
            }
        } else {
            tipoU = operandoU.getTipo(tabla, arbol);
            if (tipoU instanceof Excepcion) {
                return tipoU;
            } else if (!(tipoU instanceof Tipo)) {
                tipoResultante = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Ha habido un error al realizar la operacion, opU", fila, columna);
                arbol.getErrores().add((Excepcion) tipoResultante);
                return tipoResultante;
            }
        }

        switch (operador) {
            case SUMA:
                tipoResultante = getTipoSuma((Tipo) tipo1, (Tipo) tipo2);
                break;
            case RESTA:
                tipoResultante = getTipoResta((Tipo) tipo1, (Tipo) tipo2);
                break;
            case MULTIPLICACION:
                tipoResultante = getTipoMultiplicacion((Tipo) tipo1, (Tipo) tipo2);
                break;
            case DIVISION:
                tipoResultante = getTipoDivison((Tipo) tipo1, (Tipo) tipo2);
                break;
            case MODULO:
                tipoResultante = getTipoModulo((Tipo) tipo1, (Tipo) tipo2);
                break;
            case POTENCIA:
                tipoResultante = getTipoPotencia((Tipo) tipo1, (Tipo) tipo2);
                break;
            case MENOR_QUE:
                tipoResultante = getTipoRelacional((Tipo) tipo1, (Tipo) tipo2);
                break;
            case MENOR_IGUAL:
                tipoResultante = getTipoRelacional((Tipo) tipo1, (Tipo) tipo2);
                break;
            case MAYOR_QUE:
                tipoResultante = getTipoRelacional((Tipo) tipo1, (Tipo) tipo2);
                break;
            case MAYOR_IGUAL:
                tipoResultante = getTipoRelacional((Tipo) tipo1, (Tipo) tipo2);
                break;
            case IGUAL_IGUAL:
                tipoResultante = getTipoRelacionalIgualdad((Tipo) tipo1, (Tipo) tipo2);
                break;
            case DIFERENTE_QUE:
                tipoResultante = getTipoRelacionalIgualdad((Tipo) tipo1, (Tipo) tipo2);
                break;
            case AND:
                tipoResultante = getTipoLogico((Tipo) tipo1, (Tipo) tipo2);
                break;
            case OR:
                tipoResultante = getTipoLogico((Tipo) tipo1, (Tipo) tipo2);
                break;
            case NOR:
                tipoResultante = getTipoLogico((Tipo) tipo1, (Tipo) tipo2);
                break;
            case NAND:
                tipoResultante = getTipoLogico((Tipo) tipo1, (Tipo) tipo2);
                break;
            case NOT:
                tipoResultante = getTipoLogicoNOT((Tipo) tipoU);
                break;
        }
        if (tipoResultante instanceof Excepcion) {
            arbol.getErrores().add((Excepcion) tipoResultante);
        } else if (!(tipoResultante instanceof Tipo)) {
            tipoResultante = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Ha habido un error al realizar la operacion, op2", fila, columna);
            arbol.getErrores().add((Excepcion) tipoResultante);
            return tipoResultante;
        }
        return tipoResultante;
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    Object getTipoSuma(Tipo t1, Tipo t2) {
        //INTEGER
        if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.CHAR) {
            return new Tipo(tipo.INTEGER);
        } //REAL
        else if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.REAL && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.REAL) {
            return new Tipo(tipo.REAL);
        } //WORD
        else if (t1.getType() == tipo.WORD && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.WORD
                || t1.getType() == tipo.WORD && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.WORD
                || t1.getType() == tipo.WORD && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.WORD
                || t1.getType() == tipo.WORD && t2.getType() == tipo.BOOLEAN
                || t1.getType() == tipo.BOOLEAN && t2.getType() == tipo.WORD
                || t1.getType() == tipo.WORD && t2.getType() == tipo.WORD) {
            return new Tipo(tipo.WORD);
        } //STRING
        else if (t1.getType() == tipo.STRING && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.STRING
                || t1.getType() == tipo.STRING && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.STRING
                || t1.getType() == tipo.STRING && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.STRING
                || t1.getType() == tipo.STRING && t2.getType() == tipo.BOOLEAN
                || t1.getType() == tipo.BOOLEAN && t2.getType() == tipo.STRING
                || t1.getType() == tipo.STRING && t2.getType() == tipo.WORD
                || t1.getType() == tipo.WORD && t2.getType() == tipo.STRING
                || t1.getType() == tipo.STRING && t2.getType() == tipo.STRING) {
            return new Tipo(tipo.STRING);
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos " + t1.getType() + " y " + t2.getType() + ""
                    + " no se pueden utilizar con el operador SUMA.", fila, columna);
        }
    }

    Object getTipoResta(Tipo t1, Tipo t2) {
        //INTEGER
        if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.CHAR) {
            return new Tipo(tipo.INTEGER);
        } //REAL
        else if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.REAL && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.REAL) {
            return new Tipo(tipo.REAL);
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos " + t1.getType() + " y " + t2.getType() + ""
                    + " no se pueden utilizar con el operador RESTA.", fila, columna);
        }
    }

    Object getTipoMultiplicacion(Tipo t1, Tipo t2) {
        //INTEGER
        if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.CHAR) {
            return new Tipo(tipo.INTEGER);
        } //REAL
        else if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.REAL && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.REAL) {
            return new Tipo(tipo.REAL);
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos " + t1.getType() + " y " + t2.getType() + ""
                    + " no se pueden utilizar con el operador MULTIPLICACION.", fila, columna);
        }
    }

    Object getTipoPotencia(Tipo t1, Tipo t2) {
        //INTEGER
        if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.CHAR) {
            return new Tipo(tipo.INTEGER);
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos " + t1.getType() + " y " + t2.getType() + ""
                    + " no se pueden utilizar con el operador POTENCIA.", fila, columna);
        }
    }

    Object getTipoModulo(Tipo t1, Tipo t2) {
        //INTEGER
        if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.CHAR) {
            return new Tipo(tipo.INTEGER);
        } //REAL
        else if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.REAL && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.REAL) {
            return new Tipo(tipo.REAL);
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos " + t1.getType() + " y " + t2.getType() + ""
                    + " no se pueden utilizar con el operador MODULO.", fila, columna);
        }
    }

    Object getTipoDivison(Tipo t1, Tipo t2) {
        //INTEGER
        if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.CHAR) {
            return new Tipo(tipo.INTEGER);
        } //REAL
        else if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.REAL && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.REAL) {
            return new Tipo(tipo.REAL);
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos " + t1.getType() + " y " + t2.getType() + ""
                    + " no se pueden utilizar con el operador DIVISION.", fila, columna);
        }
    }

    Object getTipoRelacional(Tipo t1, Tipo t2) {
        if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.REAL && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.REAL
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.REAL && t2.getType() == tipo.REAL
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.CHAR) {
            return new Tipo(tipo.BOOLEAN);
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos " + t1.getType() + " y " + t2.getType() + ""
                    + " no se pueden utilizar con el operador RELACIONAL " + operador + ".", fila, columna);
        }
    }

    Object getTipoRelacionalIgualdad(Tipo t1, Tipo t2) {
        if (t1.getType() == tipo.INTEGER && t2.getType() == tipo.REAL
                || t1.getType() == tipo.REAL && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.REAL && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.REAL
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.REAL && t2.getType() == tipo.REAL
                || t1.getType() == tipo.INTEGER && t2.getType() == tipo.INTEGER
                || t1.getType() == tipo.CHAR && t2.getType() == tipo.CHAR
                || t1.getType() == tipo.STRING && t2.getType() == tipo.STRING
                || t1.getType() == tipo.STRING && t2.getType() == tipo.WORD
                || t1.getType() == tipo.WORD && t2.getType() == tipo.STRING
                || t1.getType() == tipo.WORD && t2.getType() == tipo.WORD
                || t1.getType() == tipo.BOOLEAN && t2.getType() == tipo.BOOLEAN) {
            return new Tipo(tipo.BOOLEAN);
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos " + t1.getType() + " y " + t2.getType() + ""
                    + " no se pueden utilizar con el operador RELACIONAL " + operador + ".", fila, columna);
        }
    }

    Object getTipoLogico(Tipo t1, Tipo t2) {
        if (t1.getType() == tipo.BOOLEAN && t2.getType() == tipo.BOOLEAN) {
            return new Tipo(tipo.BOOLEAN);
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos " + t1.getType() + " y " + t2.getType() + ""
                    + " no se pueden utilizar con el operador Logico " + operador + ".", fila, columna);
        }
    }

    Object getTipoLogicoNOT(Tipo t1) {
        if (t1.getType() == tipo.BOOLEAN) {
            return new Tipo(tipo.BOOLEAN);
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, el tipo " + t1.getType() + ""
                    + " no se puede utilizar con el operador Logico NOT.", fila, columna);
        }
    }

}
