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
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Inicio operacion linea: " + fila + ", columna: " + columna + "\n", op1Actual = "", op2Actual = "", opUActual = "";
        Object tipoOP1 = null, tipoOP2 = null, tipoOPU = null;
        Object op1 = null, op2 = null, opU = null;
        Tipo tipo1 = null, tipo2 = null, tipoU = null;
        switch (operador) {
            case SUMA:
                codigo += get4DSuma(tabla, arbol, operando1, operando2);
                break;
            case RESTA:
                codigo += get4DResta(tabla, arbol, operando1, operando2);
                break;
            case MULTIPLICACION:
                codigo += get4DMultiplicacion(tabla, arbol, operando1, operando2);
                break;
            case DIVISION:
                codigo += get4DDivision(tabla, arbol, operando1, operando2);
                break;
            case MODULO:
                codigo += get4DModulo(tabla, arbol, operando1, operando2);
                break;
            case POTENCIA:
                codigo += get4DPotencia(tabla, arbol, operando1, operando2);
                break;
            case MENOR_QUE:
                codigo += get4DMenorQue(tabla, arbol, operando1, operando2);
                break;
            case MENOR_IGUAL:
                codigo += get4DMenorIgual(tabla, arbol, operando1, operando2);
                break;
            case MAYOR_QUE:
                codigo += get4DMayorQue(tabla, arbol, operando1, operando2);
                break;
            case MAYOR_IGUAL:
                codigo += get4DMayorIgual(tabla, arbol, operando1, operando2);
                break;
            case IGUAL_IGUAL:
                codigo += get4DIgualIgual(tabla, arbol, operando1, operando2);
                break;
            case DIFERENTE_QUE:
                codigo += get4DDiferente(tabla, arbol, operando1, operando2);
                break;
            case AND:
                codigo += get4DAND(tabla, arbol, operando1, operando2);
                break;
            case OR:
                codigo += get4DOR(tabla, arbol, operando1, operando2);
                break;
            case NOR:
                codigo += get4DNOR(tabla, arbol, operando1, operando2);
                break;
            case NAND:
                codigo += get4DNAND(tabla, arbol, operando1, operando2);
                break;
            case NOT:
                codigo += get4DNOT(tabla, arbol, operandoU);
                break;
            case MENOS_UNARIO:
                codigo += get4DNEGAR(tabla, arbol, operandoU);
                break;
        }
        codigo += "// Fin operacion\n";
        return codigo;
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        Object tipo1 = null, tipo2 = null, tipoU = null;
        Tipo tipoOP1 = null, tipoOP2 = null, tipoOPU = null;
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
            tipoOP1 = (Tipo) tipo1;
            tipoOP1 = tipoOP1.verificarUserType(tabla);
            if (tipoOP1.getType() == tipo.RANGE) {
                tipoOP1.setType(tipoOP1.getTipoRange());
            }
            tipo2 = operando2.getTipo(tabla, arbol);
            if (tipo2 instanceof Excepcion) {
                return tipo2;
            } else if (!(tipo2 instanceof Tipo)) {
                tipoResultante = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Ha habido un error al realizar la operacion, op2", fila, columna);
                arbol.getErrores().add((Excepcion) tipoResultante);
                return tipoResultante;
            }
            tipoOP2 = (Tipo) tipo2;
            tipoOP2 = tipoOP2.verificarUserType(tabla);
            if (tipoOP2.getType() == tipo.RANGE) {
                tipoOP2.setType(tipoOP2.getTipoRange());
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
            tipoOPU = (Tipo) tipoU;
            tipoOPU = tipoOPU.verificarUserType(tabla);
        }

        switch (operador) {
            case SUMA:
                tipoResultante = getTipoSuma(tipoOP1, tipoOP2);
                break;
            case RESTA:
                tipoResultante = getTipoResta(tipoOP1, tipoOP2);
                break;
            case MULTIPLICACION:
                tipoResultante = getTipoMultiplicacion(tipoOP1, tipoOP2);
                break;
            case DIVISION:
                tipoResultante = getTipoDivison(tipoOP1, tipoOP2);
                break;
            case MODULO:
                tipoResultante = getTipoModulo(tipoOP1, tipoOP2);
                break;
            case POTENCIA:
                tipoResultante = getTipoPotencia(tipoOP1, tipoOP2);
                break;
            case MENOR_QUE:
                tipoResultante = getTipoRelacional(tipoOP1, tipoOP2);
                break;
            case MENOR_IGUAL:
                tipoResultante = getTipoRelacional(tipoOP1, tipoOP2);
                break;
            case MAYOR_QUE:
                tipoResultante = getTipoRelacional(tipoOP1, tipoOP2);
                break;
            case MAYOR_IGUAL:
                tipoResultante = getTipoRelacional(tipoOP1, tipoOP2);
                break;
            case IGUAL_IGUAL:
                tipoResultante = getTipoRelacionalIgualdad(tipoOP1, tipoOP2);
                break;
            case DIFERENTE_QUE:
                tipoResultante = getTipoRelacionalIgualdad(tipoOP1, tipoOP2);
                break;
            case AND:
                tipoResultante = getTipoLogico(tipoOP1, tipoOP2);
                break;
            case OR:
                tipoResultante = getTipoLogico(tipoOP1, tipoOP2);
                break;
            case NOR:
                tipoResultante = getTipoLogico(tipoOP1, tipoOP2);
                break;
            case NAND:
                tipoResultante = getTipoLogico(tipoOP1, tipoOP2);
                break;
            case NOT:
                tipoResultante = getTipoLogicoNOT(tipoOPU);
                break;
            case MENOS_UNARIO:
                tipoResultante = getTipoMenosUnario(tipoOPU);
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
                || t1.getType() == tipo.BOOLEAN && t2.getType() == tipo.BOOLEAN
                || t1.getType() == tipo.NIL && t2.getType() == tipo.RECORD
                || t1.getType() == tipo.RECORD && t2.getType() == tipo.NIL
                || ((t1.getType() == tipo.ENUMERADO && t2.getType() == tipo.ENUMERADO) && (t1.equals(t2)))) {
            return new Tipo(tipo.BOOLEAN);
        } else {
            if (t1.getType() == tipo.ENUMERADO && t2.getType() == tipo.ENUMERADO) {
                return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos Enum_" + t1.getNombreEnum() + " y Enum_" + t2.getNombreEnum() + ""
                        + " no se pueden comparar.", fila, columna);
            } else {
                return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, los tipos " + t1.getType() + " y " + t2.getType() + ""
                        + " no se pueden utilizar con el operador RELACIONAL " + operador + ".", fila, columna);
            }

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

    Object getTipoMenosUnario(Tipo t1) {
        if (t1.getType() == tipo.INTEGER || t1.getType() == tipo.REAL) {
            return t1;
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, el tipo " + t1.getType() + ""
                    + " no se puede utilizar con el operador Menos Unario.", fila, columna);
        }
    }

    String get4DSuma(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);
        Tipo tipo1 = (Tipo) op1.getTipo(tabla, arbol);
        tipo1 = tipo1.verificarUserType(tabla);
        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);
        Tipo tipo2 = (Tipo) op1.getTipo(tabla, arbol);
        tipo2 = tipo2.verificarUserType(tabla);
        if ((tipo1.getType() == Tipo.tipo.STRING || tipo1.getType() == tipo.WORD) && tipo2.getType() == tipo.INTEGER) {
            String temp10 = tabla.getTemporal();
            String temp11 = tabla.getTemporal();
            String temp12 = tabla.getTemporal();
            String temp13 = tabla.getTemporal();
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            String temp3 = tabla.getTemporal();
            String temp4 = tabla.getTemporal();
            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            tabla.AgregarTemporal(temp10);
            tabla.AgregarTemporal(temp11);
            tabla.AgregarTemporal(temp12);
            tabla.AgregarTemporal(temp13);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
            codigo += "+,p,0," + temp10 + "\n";
            tabla.AgregarTemporal(temp10);
            codigo += "=," + temp10 + ",-1,stack\n";
            tabla.QuitarTemporal(temp10);

            codigo += "+,p,1," + temp11 + "\n";
            tabla.AgregarTemporal(temp11);
            codigo += "=," + temp11 + "," + op2Actual + ",stack\n";
            tabla.QuitarTemporal(temp11);
            tabla.QuitarTemporal(op2Actual);

            codigo += "call,,,proc_intToString\n";
            codigo += "+,p,0," + temp12 + "\n";
            tabla.AgregarTemporal(temp12);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";

            codigo += "=,stack," + temp12 + "," + temp13 + "\n";


            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + "," + temp1 + "\n";
            tabla.AgregarTemporal(temp1);
            codigo += "+," + temp1 + ",1," + temp2 + "\n";
            tabla.AgregarTemporal(temp2);
            tabla.QuitarTemporal(temp1);
            codigo += "=," + temp2 + "," + op1Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp2);
            tabla.QuitarTemporal(op1Actual);
            codigo += "+," + temp1 + ",2," + temp3 + "\n";
            tabla.AgregarTemporal(temp3);
            codigo += "=," + temp3 + "," + temp13 + ",stack" + "\n";
            tabla.QuitarTemporal(temp3);
            tabla.QuitarTemporal(op2Actual);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
            codigo += "call,,,concatenar_cadenas" + "\n";
            codigo += "+,p,0," + temp4 + "\n";
            tabla.AgregarTemporal(temp4);
            codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(temp4);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";

        } else if (tipo1.getType() == tipo.INTEGER && (tipo2.getType() == Tipo.tipo.STRING || tipo2.getType() == tipo.WORD)) {
            String temp10 = tabla.getTemporal();
            String temp11 = tabla.getTemporal();
            String temp12 = tabla.getTemporal();
            String temp13 = tabla.getTemporal();
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            String temp3 = tabla.getTemporal();
            String temp4 = tabla.getTemporal();
            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            tabla.AgregarTemporal(temp10);
            tabla.AgregarTemporal(temp11);
            tabla.AgregarTemporal(temp12);
            tabla.AgregarTemporal(temp13);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
            codigo += "+,p,0," + temp10 + "\n";
            tabla.AgregarTemporal(temp10);
            codigo += "=," + temp10 + ",-1,stack\n";
            tabla.QuitarTemporal(temp10);

            codigo += "+,p,1," + temp11 + "\n";
            tabla.AgregarTemporal(temp11);
            codigo += "=," + temp11 + "," + op1Actual + ",stack\n";
            tabla.QuitarTemporal(temp11);
            tabla.QuitarTemporal(op1Actual);

            codigo += "call,,,proc_intToString\n";
            codigo += "+,p,0," + temp12 + "\n";
            tabla.AgregarTemporal(temp12);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";

            codigo += "=,stack," + temp12 + "," + temp13 + "\n";


            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + "," + temp1 + "\n";
            tabla.AgregarTemporal(temp1);
            codigo += "+," + temp1 + ",1," + temp2 + "\n";
            tabla.AgregarTemporal(temp2);
            tabla.QuitarTemporal(temp1);
            codigo += "=," + temp2 + "," + temp13 + ",stack" + "\n";
            tabla.QuitarTemporal(temp2);
            tabla.QuitarTemporal(op1Actual);
            codigo += "+," + temp1 + ",2," + temp3 + "\n";
            tabla.AgregarTemporal(temp3);
            codigo += "=," + temp3 + "," + op2Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp3);
            tabla.QuitarTemporal(op2Actual);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
            codigo += "call,,,concatenar_cadenas" + "\n";
            codigo += "+,p,0," + temp4 + "\n";
            tabla.AgregarTemporal(temp4);
            codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(temp4);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";

        } else if ((tipo1.getType() == Tipo.tipo.STRING || tipo1.getType() == tipo.WORD) && tipo2.getType() == tipo.REAL) {
            String temp10 = tabla.getTemporal();
            String temp11 = tabla.getTemporal();
            String temp12 = tabla.getTemporal();
            String temp13 = tabla.getTemporal();
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            String temp3 = tabla.getTemporal();
            String temp4 = tabla.getTemporal();
            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            tabla.AgregarTemporal(temp10);
            tabla.AgregarTemporal(temp11);
            tabla.AgregarTemporal(temp12);
            tabla.AgregarTemporal(temp13);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
            codigo += "+,p,0," + temp10 + "\n";
            tabla.AgregarTemporal(temp10);
            codigo += "=," + temp10 + ",-1,stack\n";
            tabla.QuitarTemporal(temp10);

            codigo += "+,p,1," + temp11 + "\n";
            tabla.AgregarTemporal(temp11);
            codigo += "=," + temp11 + "," + op2Actual + ",stack\n";
            tabla.QuitarTemporal(temp11);
            tabla.QuitarTemporal(op2Actual);

            codigo += "call,,,realToStringPrimitiva\n";
            codigo += "+,p,0," + temp12 + "\n";
            tabla.AgregarTemporal(temp12);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";

            codigo += "=,stack," + temp12 + "," + temp13 + "\n";


            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + "," + temp1 + "\n";
            tabla.AgregarTemporal(temp1);
            codigo += "+," + temp1 + ",1," + temp2 + "\n";
            tabla.AgregarTemporal(temp2);
            tabla.QuitarTemporal(temp1);
            codigo += "=," + temp2 + "," + op1Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp2);
            tabla.QuitarTemporal(op1Actual);
            codigo += "+," + temp1 + ",2," + temp3 + "\n";
            tabla.AgregarTemporal(temp3);
            codigo += "=," + temp3 + "," + temp13 + ",stack" + "\n";
            tabla.QuitarTemporal(temp3);
            tabla.QuitarTemporal(op2Actual);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
            codigo += "call,,,concatenar_cadenas" + "\n";
            codigo += "+,p,0," + temp4 + "\n";
            tabla.AgregarTemporal(temp4);
            codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(temp4);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";

        } else if (tipo1.getType() == tipo.REAL && (tipo2.getType() == Tipo.tipo.STRING || tipo2.getType() == tipo.WORD)) {
            String temp10 = tabla.getTemporal();
            String temp11 = tabla.getTemporal();
            String temp12 = tabla.getTemporal();
            String temp13 = tabla.getTemporal();
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            String temp3 = tabla.getTemporal();
            String temp4 = tabla.getTemporal();
            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            tabla.AgregarTemporal(temp10);
            tabla.AgregarTemporal(temp11);
            tabla.AgregarTemporal(temp12);
            tabla.AgregarTemporal(temp13);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
            codigo += "+,p,0," + temp10 + "\n";
            tabla.AgregarTemporal(temp10);
            codigo += "=," + temp10 + ",-1,stack\n";
            tabla.QuitarTemporal(temp10);

            codigo += "+,p,1," + temp11 + "\n";
            tabla.AgregarTemporal(temp11);
            codigo += "=," + temp11 + "," + op1Actual + ",stack\n";
            tabla.QuitarTemporal(temp11);
            tabla.QuitarTemporal(op1Actual);

            codigo += "call,,,realToStringPrimitiva\n";
            codigo += "+,p,0," + temp12 + "\n";
            tabla.AgregarTemporal(temp12);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";

            codigo += "=,stack," + temp12 + "," + temp13 + "\n";


            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + "," + temp1 + "\n";
            tabla.AgregarTemporal(temp1);
            codigo += "+," + temp1 + ",1," + temp2 + "\n";
            tabla.AgregarTemporal(temp2);
            tabla.QuitarTemporal(temp1);
            codigo += "=," + temp2 + "," + temp13 + ",stack" + "\n";
            tabla.QuitarTemporal(temp2);
            tabla.QuitarTemporal(op1Actual);
            codigo += "+," + temp1 + ",2," + temp3 + "\n";
            tabla.AgregarTemporal(temp3);
            codigo += "=," + temp3 + "," + op2Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp3);
            tabla.QuitarTemporal(op2Actual);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
            codigo += "call,,,concatenar_cadenas" + "\n";
            codigo += "+,p,0," + temp4 + "\n";
            tabla.AgregarTemporal(temp4);
            codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(temp4);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";

        } else if ((tipo1.getType() == Tipo.tipo.STRING || tipo1.getType() == tipo.WORD) && tipo2.getType() == tipo.BOOLEAN) {
            String temp10 = tabla.getTemporal();
            String temp11 = tabla.getTemporal();
            String temp12 = tabla.getTemporal();
            String temp13 = tabla.getTemporal();
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            String temp3 = tabla.getTemporal();
            String temp4 = tabla.getTemporal();
            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            tabla.AgregarTemporal(temp10);
            tabla.AgregarTemporal(temp11);
            tabla.AgregarTemporal(temp12);
            tabla.AgregarTemporal(temp13);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
            codigo += "+,p,0," + temp10 + "\n";
            tabla.AgregarTemporal(temp10);
            codigo += "=," + temp10 + ",-1,stack\n";
            tabla.QuitarTemporal(temp10);

            codigo += "+,p,1," + temp11 + "\n";
            tabla.AgregarTemporal(temp11);
            codigo += "=," + temp11 + "," + op2Actual + ",stack\n";
            tabla.QuitarTemporal(temp11);
            tabla.QuitarTemporal(op2Actual);

            codigo += "call,,,booleanToStringPrimitiva\n";
            codigo += "+,p,0," + temp12 + "\n";
            tabla.AgregarTemporal(temp12);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";

            codigo += "=,stack," + temp12 + "," + temp13 + "\n";


            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + "," + temp1 + "\n";
            tabla.AgregarTemporal(temp1);
            codigo += "+," + temp1 + ",1," + temp2 + "\n";
            tabla.AgregarTemporal(temp2);
            tabla.QuitarTemporal(temp1);
            codigo += "=," + temp2 + "," + op1Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp2);
            tabla.QuitarTemporal(op1Actual);
            codigo += "+," + temp1 + ",2," + temp3 + "\n";
            tabla.AgregarTemporal(temp3);
            codigo += "=," + temp3 + "," + temp13 + ",stack" + "\n";
            tabla.QuitarTemporal(temp3);
            tabla.QuitarTemporal(op2Actual);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
            codigo += "call,,,concatenar_cadenas" + "\n";
            codigo += "+,p,0," + temp4 + "\n";
            tabla.AgregarTemporal(temp4);
            codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(temp4);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";

        } else if (tipo1.getType() == tipo.BOOLEAN && (tipo2.getType() == Tipo.tipo.STRING || tipo2.getType() == tipo.WORD)) {
            String temp10 = tabla.getTemporal();
            String temp11 = tabla.getTemporal();
            String temp12 = tabla.getTemporal();
            String temp13 = tabla.getTemporal();
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            String temp3 = tabla.getTemporal();
            String temp4 = tabla.getTemporal();
            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            tabla.AgregarTemporal(temp10);
            tabla.AgregarTemporal(temp11);
            tabla.AgregarTemporal(temp12);
            tabla.AgregarTemporal(temp13);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
            codigo += "+,p,0," + temp10 + "\n";
            tabla.AgregarTemporal(temp10);
            codigo += "=," + temp10 + ",-1,stack\n";
            tabla.QuitarTemporal(temp10);

            codigo += "+,p,1," + temp11 + "\n";
            tabla.AgregarTemporal(temp11);
            codigo += "=," + temp11 + "," + op1Actual + ",stack\n";
            tabla.QuitarTemporal(temp11);
            tabla.QuitarTemporal(op1Actual);

            codigo += "call,,,booleanToStringPrimitiva\n";
            codigo += "+,p,0," + temp12 + "\n";
            tabla.AgregarTemporal(temp12);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";

            codigo += "=,stack," + temp12 + "," + temp13 + "\n";


            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + "," + temp1 + "\n";
            tabla.AgregarTemporal(temp1);
            codigo += "+," + temp1 + ",1," + temp2 + "\n";
            tabla.AgregarTemporal(temp2);
            tabla.QuitarTemporal(temp1);
            codigo += "=," + temp2 + "," + temp13 + ",stack" + "\n";
            tabla.QuitarTemporal(temp2);
            tabla.QuitarTemporal(op1Actual);
            codigo += "+," + temp1 + ",2," + temp3 + "\n";
            tabla.AgregarTemporal(temp3);
            codigo += "=," + temp3 + "," + op2Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp3);
            tabla.QuitarTemporal(op2Actual);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
            codigo += "call,,,concatenar_cadenas" + "\n";
            codigo += "+,p,0," + temp4 + "\n";
            tabla.AgregarTemporal(temp4);
            codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(temp4);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";

        } else if ((tipo1.getType() == Tipo.tipo.STRING || tipo1.getType() == tipo.WORD) && (tipo2.getType() == Tipo.tipo.STRING || tipo2.getType() == tipo.WORD)) {
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            String temp3 = tabla.getTemporal();
            String temp4 = tabla.getTemporal();
            tabla.AgregarTemporal(temp1);
            tabla.AgregarTemporal(temp2);
            tabla.AgregarTemporal(temp3);
            tabla.AgregarTemporal(temp4);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + "," + temp1 + "\n";
            tabla.AgregarTemporal(temp1);
            codigo += "+," + temp1 + ",1," + temp2 + "\n";
            tabla.AgregarTemporal(temp2);
            tabla.QuitarTemporal(temp1);
            codigo += "=," + temp2 + "," + op1Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp2);
            tabla.QuitarTemporal(op1Actual);
            codigo += "+," + temp1 + ",2," + temp3 + "\n";
            tabla.AgregarTemporal(temp3);
            codigo += "=," + temp3 + "," + op2Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp3);
            tabla.QuitarTemporal(op2Actual);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
            codigo += "call,,,concatenar_cadenas" + "\n";
            codigo += "+,p,0," + temp4 + "\n";
            tabla.AgregarTemporal(temp4);
            codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(temp4);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
        } else if ((tipo1.getType() == Tipo.tipo.STRING || tipo1.getType() == tipo.WORD) && tipo2.getType() == Tipo.tipo.CHAR) {
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            String temp3 = tabla.getTemporal();
            String temp4 = tabla.getTemporal();
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + "," + temp1 + "\n";
            tabla.AgregarTemporal(temp1);
            codigo += "+," + temp1 + ",1," + temp2 + "\n";
            tabla.AgregarTemporal(temp2);
            tabla.QuitarTemporal(temp1);
            codigo += "=," + temp2 + "," + op1Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp2);
            tabla.QuitarTemporal(op1Actual);
            codigo += "+," + temp1 + ",2," + temp3 + "\n";
            tabla.AgregarTemporal(temp3);
            codigo += "=," + temp3 + "," + op2Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp3);
            tabla.QuitarTemporal(op2Actual);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
            codigo += "call,,,concatenar_Stringchar" + "\n";
            codigo += "+,p,0," + temp4 + "\n";
            tabla.AgregarTemporal(temp4);
            codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(temp4);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
        } else if (tipo1.getType() == tipo.CHAR && (tipo2.getType() == tipo.STRING || tipo2.getType() == tipo.WORD)) {
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            String temp3 = tabla.getTemporal();
            String temp4 = tabla.getTemporal();
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + "," + temp1 + "\n";
            tabla.AgregarTemporal(temp1);
            codigo += "+," + temp1 + ",1," + temp2 + "\n";
            tabla.AgregarTemporal(temp2);
            tabla.QuitarTemporal(temp1);
            codigo += "=," + temp2 + "," + op1Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp2);
            tabla.QuitarTemporal(op1Actual);
            codigo += "+," + temp1 + ",2," + temp3 + "\n";
            tabla.AgregarTemporal(temp3);
            codigo += "=," + temp3 + "," + op2Actual + ",stack" + "\n";
            tabla.QuitarTemporal(temp3);
            tabla.QuitarTemporal(op2Actual);
            codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
            codigo += "call,,,concatenar_Stringchar" + "\n";
            codigo += "+,p,0," + temp4 + "\n";
            tabla.AgregarTemporal(temp4);
            codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(temp4);
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p" + "\n";
        } else {
            codigo += "+," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(op1Actual);
            tabla.QuitarTemporal(op2Actual);
        }
        return codigo;
    }

    String get4DResta(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);

        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);

        codigo += "-," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(op1Actual);
        tabla.QuitarTemporal(op2Actual);
        return codigo;
    }

    String get4DMultiplicacion(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);

        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);

        codigo += "*," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(op1Actual);
        tabla.QuitarTemporal(op2Actual);
        return codigo;
    }

    String get4DModulo(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);

        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);

        codigo += "%," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(op1Actual);
        tabla.QuitarTemporal(op2Actual);
        return codigo;
    }

    String get4DPotencia(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);

        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);

        String temp1Pot = tabla.getTemporal();
        String temp2Pot = tabla.getTemporal();
        String temp3Pot = tabla.getTemporal();
        tabla.AgregarTemporal(temp1Pot);
        tabla.AgregarTemporal(temp2Pot);
        tabla.AgregarTemporal(temp3Pot);
        codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "+,p,1," + temp1Pot + "\n";
        tabla.AgregarTemporal(temp1Pot);
        codigo += "+,p,2," + temp2Pot + "\n";
        tabla.AgregarTemporal(temp2Pot);
        codigo += "=," + temp1Pot + "," + op1Actual + ",stack\n";
        tabla.QuitarTemporal(temp1Pot);
        tabla.QuitarTemporal(op1Actual);
        codigo += "=," + temp2Pot + "," + op2Actual + ",stack\n";
        tabla.QuitarTemporal(temp2Pot);
        tabla.QuitarTemporal(op2Actual);
        codigo += "call,,,potencia_primitiva\n";
        codigo += "+,p,0," + temp3Pot + "\n";
        tabla.AgregarTemporal(temp3Pot);
        codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "=,stack," + temp3Pot + "," + tabla.getTemporal() + "\n";
        tabla.QuitarTemporal(temp3Pot);
        tabla.AgregarTemporal(tabla.getTemporalActual());
        return codigo;
    }

    String get4DMenorQue(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);

        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);

        String temp1 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();
        codigo += "jl," + op1Actual + "," + op2Actual + "," + label1 + "\n";
        tabla.QuitarTemporal(op1Actual);
        tabla.QuitarTemporal(op2Actual);
        codigo += "=,0,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += "jmp,,," + label2 + "\n";
        codigo += label1 + ":\n";
        codigo += "=,1,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += label2 + ":\n";
        codigo += "=," + temp1 + ",," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(temp1);
        return codigo;
    }

    String get4DMayorQue(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);

        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);

        String temp1 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();
        codigo += "jg," + op1Actual + "," + op2Actual + "," + label1 + "\n";
        tabla.QuitarTemporal(op1Actual);
        tabla.QuitarTemporal(op2Actual);
        codigo += "=,0,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += "jmp,,," + label2 + "\n";
        codigo += label1 + ":\n";
        codigo += "=,1,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += label2 + ":\n";
        codigo += "=," + temp1 + ",," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(temp1);
        return codigo;
    }

    String get4DMenorIgual(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);

        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);

        String temp1 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();
        codigo += "jle," + op1Actual + "," + op2Actual + "," + label1 + "\n";
        tabla.QuitarTemporal(op1Actual);
        tabla.QuitarTemporal(op2Actual);
        codigo += "=,0,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += "jmp,,," + label2 + "\n";
        codigo += label1 + ":\n";
        codigo += "=,1,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += label2 + ":\n";
        codigo += "=," + temp1 + ",," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(temp1);
        return codigo;
    }

    String get4DMayorIgual(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);

        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);

        String temp1 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();
        codigo += "jge," + op1Actual + "," + op2Actual + "," + label1 + "\n";
        tabla.QuitarTemporal(op1Actual);
        tabla.QuitarTemporal(op2Actual);
        codigo += "=,0,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += "jmp,,," + label2 + "\n";
        codigo += label1 + ":\n";
        codigo += "=,1,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += label2 + ":\n";
        codigo += "=," + temp1 + ",," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(temp1);
        return codigo;
    }

    String get4DIgualIgual(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);

        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);

        String temp1 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();
        codigo += "je," + op1Actual + "," + op2Actual + "," + label1 + "\n";
        tabla.QuitarTemporal(op1Actual);
        tabla.QuitarTemporal(op2Actual);
        codigo += "=,0,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += "jmp,,," + label2 + "\n";
        codigo += label1 + ":\n";
        codigo += "=,1,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += label2 + ":\n";
        codigo += "=," + temp1 + ",," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(temp1);
        return codigo;
    }

    String get4DDiferente(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);

        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);

        String temp1 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();
        codigo += "jne," + op1Actual + "," + op2Actual + "," + label1 + "\n";
        tabla.QuitarTemporal(op1Actual);
        tabla.QuitarTemporal(op2Actual);
        codigo += "=,0,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += "jmp,,," + label2 + "\n";
        codigo += label1 + ":\n";
        codigo += "=,1,," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += label2 + ":\n";
        codigo += "=," + temp1 + ",," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(temp1);
        return codigo;
    }

    String get4DAND(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        String etiquetaVerdadera1 = tabla.getEtiqueta();
        String etiquetaFalsa1 = tabla.getEtiqueta();
        String etiquetaVerdadera2 = tabla.getEtiqueta();
        String etiquetaFalsa2 = tabla.getEtiqueta();
        String temporalSalida = tabla.getTemporal();

        codigo += "// CONDICION 1 AND\n";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);
        codigo += "// FIN CONDICION 1 AND\n";

        codigo += "=," + 0 + ",," + temporalSalida + "\n"; //Asignar falso
        tabla.AgregarTemporal(temporalSalida);
        codigo += "je," + op1Actual + "," + 1 + "," + etiquetaVerdadera1 + "\n";
        tabla.QuitarTemporal(op1Actual);
        codigo += "jmp,,," + etiquetaFalsa1 + "\n";
        codigo += etiquetaVerdadera1 + ":\n";

        codigo += "// CONDICION 2 AND\n";
        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);
        codigo += "// FIN CONDICION 2 AND\n";

        codigo += "je," + op2Actual + "," + 1 + "," + etiquetaVerdadera2 + "\n";
        tabla.QuitarTemporal(op2Actual);
        codigo += "jmp,,," + etiquetaFalsa2 + "\n";
        codigo += etiquetaVerdadera2 + ":\n";
        codigo += "=," + 1 + ",," + temporalSalida + "\n"; //Asignar true
        tabla.AgregarTemporal(temporalSalida);
        codigo += etiquetaFalsa1 + ":\n";
        codigo += etiquetaFalsa2 + ":\n";
        codigo += "=," + temporalSalida + ",," + tabla.getTemporal() + "\n";
        return codigo;
    }

    String get4DOR(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";

        String etiquetaVerdadera1 = tabla.getEtiqueta();
        String etiquetaFalsa1 = tabla.getEtiqueta();
        String etiquetaVerdadera2 = tabla.getEtiqueta();
        String temporalSalida = tabla.getTemporal();

        codigo += "// CONDICION 1 OR\n";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);
        codigo += "// FIN CONDICION 1 OR\n";

        codigo += "=," + 0 + ",," + temporalSalida + "\n"; //Asignar falso
        tabla.AgregarTemporal(temporalSalida);
        codigo += "je," + op1Actual + "," + 1 + "," + etiquetaVerdadera1 + "\n";
        tabla.QuitarTemporal(op1Actual);

        codigo += "// CONDICION 1 OR\n";
        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);
        codigo += "// FIN CONDICION 1 OR\n";

        codigo += "je," + op2Actual + "," + 1 + "," + etiquetaVerdadera2 + "\n";
        tabla.QuitarTemporal(op2Actual);
        codigo += "jmp,,," + etiquetaFalsa1 + "\n";

        codigo += etiquetaVerdadera1 + ":\n";
        codigo += etiquetaVerdadera2 + ":\n";
        codigo += "=," + 1 + ",," + temporalSalida + "\n"; //Asignar true
        tabla.AgregarTemporal(temporalSalida);

        codigo += etiquetaFalsa1 + ":\n";
        codigo += "=," + temporalSalida + ",," + tabla.getTemporal() + "\n";
        return codigo;
    }

    String get4DNOR(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        String etiquetaVerdadera1 = tabla.getEtiqueta();
        String etiquetaFalsa1 = tabla.getEtiqueta();
        String etiquetaVerdadera2 = tabla.getEtiqueta();
        String etiquetaFalsa2 = tabla.getEtiqueta();
        String temporalSalida = tabla.getTemporal();

        codigo += "// CONDICION 1 NOR\n";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);
        codigo += "// FIN CONDICION 1 NOR\n";

        codigo += "=," + 0 + ",," + temporalSalida + "\n"; //Asignar falso
        tabla.AgregarTemporal(temporalSalida);
        codigo += "je," + op1Actual + "," + 0 + "," + etiquetaVerdadera1 + "\n";
        tabla.QuitarTemporal(op1Actual);
        codigo += "jmp,,," + etiquetaFalsa1 + "\n";
        codigo += etiquetaVerdadera1 + ":\n";

        codigo += "// CONDICION 2 NOR\n";
        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);
        codigo += "// FIN CONDICION 2 NOR\n";

        codigo += "je," + op2Actual + "," + 0 + "," + etiquetaVerdadera2 + "\n";
        tabla.QuitarTemporal(op2Actual);
        codigo += "jmp,,," + etiquetaFalsa2 + "\n";
        codigo += etiquetaVerdadera2 + ":\n";
        codigo += "=," + 1 + ",," + temporalSalida + "\n"; //Asignar true
        tabla.AgregarTemporal(temporalSalida);
        codigo += etiquetaFalsa1 + ":\n";
        codigo += etiquetaFalsa2 + ":\n";
        codigo += "=," + temporalSalida + ",," + tabla.getTemporal() + "\n";
        return codigo;
    }

    String get4DNAND(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";

        String etiquetaVerdadera1 = tabla.getEtiqueta();
        String etiquetaFalsa1 = tabla.getEtiqueta();
        String etiquetaVerdadera2 = tabla.getEtiqueta();
        String temporalSalida = tabla.getTemporal();

        codigo += "// CONDICION 1 NAND\n";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);
        codigo += "// FIN CONDICION 1 NAND\n";

        codigo += "=," + 0 + ",," + temporalSalida + "\n"; //Asignar falso
        tabla.AgregarTemporal(temporalSalida);
        codigo += "je," + op1Actual + "," + 0 + "," + etiquetaVerdadera1 + "\n";
        tabla.QuitarTemporal(op1Actual);

        codigo += "// CONDICION 1 NAND\n";
        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);
        codigo += "// FIN CONDICION 1 NAND\n";

        codigo += "je," + op2Actual + "," + 0 + "," + etiquetaVerdadera2 + "\n";
        tabla.QuitarTemporal(op2Actual);
        codigo += "jmp,,," + etiquetaFalsa1 + "\n";

        codigo += etiquetaVerdadera1 + ":\n";
        codigo += etiquetaVerdadera2 + ":\n";
        codigo += "=," + 1 + ",," + temporalSalida + "\n"; //Asignar true
        tabla.AgregarTemporal(temporalSalida);

        codigo += etiquetaFalsa1 + ":\n";
        codigo += "=," + temporalSalida + ",," + tabla.getTemporal() + "\n";
        return codigo;
    }

    String get4DNOT(Tabla tabla, Tree arbol, Expresion opU) {
        String codigo = "";
        String opUActual = "";
        codigo += opU.get4D(tabla, arbol);
        opUActual = tabla.getTemporalActual();
        tabla.AgregarTemporal(opUActual);

        String etiquetaVerdadera1 = tabla.getEtiqueta();
        String etiquetaFalsa1 = tabla.getEtiqueta();
        codigo += "je," + opUActual + "," + 1 + "," + etiquetaVerdadera1 + "\n";
        tabla.QuitarTemporal(opUActual);
        codigo += "=," + 1 + ",," + tabla.getTemporal() + "\n"; //Asignar true
        tabla.AgregarTemporal(tabla.getTemporalActual());
        codigo += "jmp,,," + etiquetaFalsa1 + "\n";
        codigo += etiquetaVerdadera1 + ":\n";
        codigo += "=," + 0 + ",," + tabla.getTemporalActual() + "\n"; //Asignar false
        tabla.AgregarTemporal(tabla.getTemporalActual());
        codigo += etiquetaFalsa1 + ":\n";
        return codigo;
    }

    String get4DNEGAR(Tabla tabla, Tree arbol, Expresion opU) {
        String codigo = "";
        String opUActual = "";
        codigo += opU.get4D(tabla, arbol);
        opUActual = tabla.getTemporalActual();
        tabla.AgregarTemporal(opUActual);

        codigo += "*," + opUActual + ",-1," + tabla.getTemporal() + "\n";
        tabla.QuitarTemporal(opUActual);
        tabla.AgregarTemporal(tabla.getTemporalActual());
        return codigo;
    }

    String get4DDivision(Tabla tabla, Tree arbol, Expresion op1, Expresion op2) {
        String codigo = "";
        String op1Actual = "";
        String op2Actual = "";
        codigo += op1.get4D(tabla, arbol);
        op1Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op1Actual);
        Tipo tipo1 = (Tipo) op1.getTipo(tabla, arbol);
        tipo1 = tipo1.verificarUserType(tabla);
        codigo += op2.get4D(tabla, arbol);
        op2Actual = tabla.getTemporalActual();
        tabla.AgregarTemporal(op2Actual);
        Tipo tipo2 = (Tipo) op1.getTipo(tabla, arbol);
        tipo2 = tipo2.verificarUserType(tabla);
        if ((tipo1.getType() == tipo.INTEGER && tipo2.getType() == tipo.INTEGER)
                || (tipo1.getType() == tipo.INTEGER && tipo2.getType() == tipo.CHAR)
                || (tipo1.getType() == tipo.CHAR && tipo2.getType() == tipo.INTEGER)
                || (tipo1.getType() == tipo.CHAR && tipo2.getType() == tipo.CHAR)) {
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            codigo += "/," + op1Actual + "," + op2Actual + "," + temp1 + "\n";
            tabla.AgregarTemporal(temp1);
            tabla.QuitarTemporal(op1Actual);
            tabla.QuitarTemporal(op2Actual);

            codigo += "%," + temp1 + ",1," + temp2 + "\n";
            tabla.AgregarTemporal(temp2);
            tabla.QuitarTemporal(temp1);

            codigo += "-," + temp1 + "," + temp2 + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(temp1);
            tabla.QuitarTemporal(temp2);
        } else {
            codigo += "/," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
            tabla.AgregarTemporal(tabla.getTemporalActual());
            tabla.QuitarTemporal(op1Actual);
            tabla.QuitarTemporal(op2Actual);
        }
        return codigo;
    }
}
