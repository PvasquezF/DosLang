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
        Object tipoOP1 = null, tipoOP2 = null, tipoOPU = null, tipoResultante = null;
        Object op1 = null, op2 = null, opU = null;
        Tipo tipo1 = null, tipo2 = null, tipoU = null;
        if (operandoU == null) {
            op1 = operando1.get4D(tabla, arbol);
            tipoOP1 = operando1.getTipo(tabla, arbol);
            if (op1 instanceof Excepcion || tipoOP1 instanceof Excepcion) {
                return op1;
            } else if (!(op1 instanceof String) || !(tipoOP1 instanceof Tipo)) {
                tipoResultante = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Ha habido un error al generar la operacion, op1", fila, columna);
                arbol.getErrores().add((Excepcion) tipoResultante);
                return tipoResultante;
            }
            codigo += op1;
            op1Actual = tabla.getTemporalActual();
            tipo1 = (Tipo) tipoOP1;
            tipo1 = tipo1.verificarUserType(tabla);

            op2 = operando2.get4D(tabla, arbol);
            tipoOP2 = operando2.getTipo(tabla, arbol);
            if (op2 instanceof Excepcion || tipoOP2 instanceof Excepcion) {
                return op2;
            } else if (!(op2 instanceof String) || !(tipoOP2 instanceof Tipo)) {
                tipoResultante = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Ha habido un error al generar la operacion, op2", fila, columna);
                arbol.getErrores().add((Excepcion) tipoResultante);
                return tipoResultante;
            }
            codigo += op2;
            op2Actual = tabla.getTemporalActual();
            tipo2 = (Tipo) tipoOP2;
            tipo2 = tipo2.verificarUserType(tabla);
        } else {
            opU = operandoU.get4D(tabla, arbol);
            tipoOPU = operandoU.getTipo(tabla, arbol);
            if (opU instanceof Excepcion || tipoOPU instanceof Excepcion) {
                return opU;
            } else if (!(opU instanceof String) || !(tipoOPU instanceof Tipo)) {
                tipoResultante = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Ha habido un error al generar la operacion, opU", fila, columna);
                arbol.getErrores().add((Excepcion) tipoResultante);
                return tipoResultante;
            }
            codigo += opU;
            opUActual = tabla.getTemporalActual();
            tipoU = (Tipo) tipoOPU;
            tipoU = tipoU.verificarUserType(tabla);
        }
        String etiquetaVerdadera1 = "";
        String etiquetaFalsa1 = "";
        String etiquetaVerdadera2 = "";
        String etiquetaFalsa2 = "";
        switch (operador) {
            case SUMA:
                codigo += get4DSuma(tabla, op1Actual, op2Actual, tipo1, tipo2);

                //codigo += "+," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
                break;
            case RESTA:
                codigo += "-," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
                break;
            case MULTIPLICACION:
                codigo += "*," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
                break;
            case DIVISION:
                codigo += "/," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
                break;
            case MODULO:
                codigo += "%," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
                break;
            case POTENCIA:
                codigo += "^," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
                break;
            case MENOR_QUE:
                String temp1 = tabla.getTemporal();
                String label1 = tabla.getEtiqueta();
                String label2 = tabla.getEtiqueta();
                codigo += "jl," + op1Actual + "," + op2Actual + "," + label1 + "\n";
                codigo += "=,0,," + temp1 + "\n";
                codigo += "jmp,,," + label2 + "\n";
                codigo += label1 + ":\n";
                codigo += "=,1,," + temp1 + "\n";
                codigo += label2 + ":\n";
                codigo += "=," + temp1 + ",," + tabla.getTemporal() + "\n";
                break;
            case MENOR_IGUAL:
                codigo += "jle," + op1Actual + "," + op2Actual + "," + tabla.getEtiqueta() + "\n";
                break;
            case MAYOR_QUE:
                codigo += "jg," + op1Actual + "," + op2Actual + "," + tabla.getEtiqueta() + "\n";
                break;
            case MAYOR_IGUAL:
                codigo += "jge," + op1Actual + "," + op2Actual + "," + tabla.getEtiqueta() + "\n";
                break;
            case IGUAL_IGUAL:
                codigo += "je," + op1Actual + "," + op2Actual + "," + tabla.getEtiqueta() + "\n";
                break;
            case DIFERENTE_QUE:
                codigo += "jne," + op1Actual + "," + op2Actual + "," + tabla.getEtiqueta() + "\n";
                break;
            case AND:
                etiquetaVerdadera1 = tabla.getEtiqueta();
                etiquetaFalsa1 = tabla.getEtiqueta();
                etiquetaVerdadera2 = tabla.getEtiqueta();
                etiquetaFalsa2 = tabla.getEtiqueta();
                codigo += "=," + 0 + ",," + tabla.getTemporal() + "\n"; //Asignar falso 
                codigo += "je," + op1Actual + "," + 1 + "," + etiquetaVerdadera1 + "\n";
                codigo += "jmp,,," + etiquetaFalsa1 + "\n";
                codigo += etiquetaVerdadera1 + ":\n";
                codigo += "je," + op2Actual + "," + 1 + "," + etiquetaVerdadera2 + "\n";
                codigo += "jmp,,," + etiquetaFalsa2 + "\n";
                codigo += etiquetaVerdadera2 + ":\n";
                codigo += "=," + 1 + ",," + tabla.getTemporalActual() + "\n"; //Asignar true 
                codigo += etiquetaFalsa1 + ":\n";
                codigo += etiquetaFalsa2 + ":\n";
                break;
            case OR:
                etiquetaVerdadera1 = tabla.getEtiqueta();
                etiquetaVerdadera2 = tabla.getEtiqueta();
                etiquetaFalsa1 = tabla.getEtiqueta();
                codigo += "=," + 0 + ",," + tabla.getTemporal() + "\n"; //Asignar falso 
                codigo += "je," + op1Actual + "," + 1 + "," + etiquetaVerdadera1 + "\n";

                codigo += "je," + op2Actual + "," + 1 + "," + etiquetaVerdadera2 + "\n";
                codigo += "jmp,,," + etiquetaFalsa1 + "\n";

                codigo += etiquetaVerdadera1 + ":\n";
                codigo += etiquetaVerdadera2 + ":\n";
                codigo += "=," + 1 + ",," + tabla.getTemporalActual() + "\n"; //Asignar true 

                codigo += etiquetaFalsa1 + ":\n";
                break;
            case NOR:
                etiquetaVerdadera1 = tabla.getEtiqueta();
                etiquetaFalsa1 = tabla.getEtiqueta();
                etiquetaVerdadera2 = tabla.getEtiqueta();
                etiquetaFalsa2 = tabla.getEtiqueta();
                codigo += "=," + 0 + ",," + tabla.getTemporal() + "\n"; //Asignar falso 
                codigo += "je," + op1Actual + "," + 0 + "," + etiquetaVerdadera1 + "\n";
                codigo += "jmp,,," + etiquetaFalsa1 + "\n";
                codigo += etiquetaVerdadera1 + ":\n";
                codigo += "je," + op2Actual + "," + 0 + "," + etiquetaVerdadera2 + "\n";
                codigo += "jmp,,," + etiquetaFalsa2 + "\n";
                codigo += etiquetaVerdadera2 + ":\n";
                codigo += "=," + 1 + ",," + tabla.getTemporalActual() + "\n"; //Asignar true 
                codigo += etiquetaFalsa1 + ":\n";
                codigo += etiquetaFalsa2 + ":\n";
                break;
            case NAND:
                etiquetaVerdadera1 = tabla.getEtiqueta();
                etiquetaVerdadera2 = tabla.getEtiqueta();
                etiquetaFalsa1 = tabla.getEtiqueta();
                codigo += "=," + 0 + ",," + tabla.getTemporal() + "\n"; //Asignar falso 
                codigo += "je," + op1Actual + "," + 0 + "," + etiquetaVerdadera1 + "\n";

                codigo += "je," + op2Actual + "," + 0 + "," + etiquetaVerdadera2 + "\n";
                codigo += "jmp,,," + etiquetaFalsa1 + "\n";

                codigo += etiquetaVerdadera1 + ":\n";
                codigo += etiquetaVerdadera2 + ":\n";
                codigo += "=," + 1 + ",," + tabla.getTemporalActual() + "\n"; //Asignar true 

                codigo += etiquetaFalsa1 + ":\n";
                break;
            case NOT:
                etiquetaVerdadera1 = tabla.getEtiqueta();
                etiquetaFalsa1 = tabla.getEtiqueta();
                codigo += "je," + opUActual + "," + 1 + "," + etiquetaVerdadera1 + "\n";
                codigo += "=," + 1 + ",," + tabla.getTemporal() + "\n"; //Asignar true 
                codigo += "jmp,,," + etiquetaFalsa1 + "\n";
                codigo += etiquetaVerdadera1 + ":\n";
                codigo += "=," + 0 + ",," + tabla.getTemporalActual() + "\n"; //Asignar false 
                codigo += etiquetaFalsa1 + ":\n";
                break;

            case MENOS_UNARIO:
                codigo += "*," + opUActual + ",-1," + tabla.getTemporal() + "\n";
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

    Object getTipoMenosUnario(Tipo t1) {
        if (t1.getType() == tipo.INTEGER || t1.getType() == tipo.REAL) {
            return t1;
        } else {
            return new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "Error, el tipo " + t1.getType() + ""
                    + " no se puede utilizar con el operador Menos Unario.", fila, columna);
        }
    }

    String get4DSuma(Tabla tabla, String op1Actual, String op2Actual, Tipo tipo1, Tipo tipo2) {
        String codigo = "";
        if (tipo1.getType() == Tipo.tipo.STRING && tipo2.getType() == Tipo.tipo.STRING) {
            String temp1 = tabla.getTemporal();
            String temp2 = tabla.getTemporal();
            String temp3 = tabla.getTemporal();
            String temp4 = tabla.getTemporal();
            codigo += "+,p," + tabla.getFuncionSizeActual() + "," + temp1 + "\n";
            codigo += "+," + temp1 + ",0," + temp2 + "\n";
            codigo += "=," + temp2 + "," + op1Actual + ",stack" + "\n";
            codigo += "+," + temp1 + ",1," + temp3 + "\n";
            codigo += "=," + temp3 + "," + op2Actual + ",stack" + "\n";
            codigo += "+,p," + tabla.getFuncionSizeActual() + ",p" + "\n";
            codigo += "call,,,concatenar_cadenas" + "\n";
            codigo += "+,p,2," + temp4 + "\n";
            codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";
            codigo += "-,p," + tabla.getFuncionSizeActual() + ",p" + "\n";
        } else {
            codigo += "+," + op1Actual + "," + op2Actual + "," + tabla.getTemporal() + "\n";
        }
        return codigo;
    }
}
