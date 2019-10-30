/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Instrucciones;

import Excepciones.Excepcion;
import Expresiones.Acceso;
import Expresiones.Malloc;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.*;

import java.util.ArrayList;

/**
 * @author Pavel
 */
public class Asignacion implements Instruccion {

    AccesoVariable variable;
    Expresion valor;
    int fila;
    int columna;

    public Asignacion(AccesoVariable variable, Expresion valor, int fila, int columna) {
        this.variable = variable;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        Object result = variable.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }

        Object resultValor = valor.getTipo(tabla, arbol);
        if (resultValor instanceof Excepcion) {
            return resultValor;
        }
        Tipo tipoVariable = (Tipo) result;
        Tipo tipoValor = (Tipo) resultValor;
        if (tipoVariable.getType() == Tipo.tipo.RANGE) {
            Object lower = tipoVariable.getLowerLimit().getTipo(tabla, arbol);
            if (lower instanceof Excepcion) {
                return lower;
            }
            Object upper = tipoVariable.getUpperLimit().getTipo(tabla, arbol);
            if (upper instanceof Excepcion) {
                return upper;
            }
            Tipo tipoLower = (Tipo) lower;
            Tipo tipoUpper = (Tipo) upper;
            if (tipoLower.equals(tipoUpper)) {
                if (!tipoLower.equals(tipoValor)) {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            "El tipo de la variable no coincide con el valor a asignar.",
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
            } else {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        "Los tipos del limite de rango no coinciden.",
                        fila, columna);
                arbol.getErrores().add(exc);
                return exc;
            }
        } else if (!tipoVariable.equals(tipoValor)) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    "El tipo de la variable no coincide con el tipo a asignar.",
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        return null;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Inicio Asignacion linea: " + fila + ", columna: " + columna + "\n";
        Object result = variable.getTipo(tabla, arbol);
        Tipo tipoVariable = (Tipo) result;
        if (tipoVariable.getType() == Tipo.tipo.INTEGER
                || tipoVariable.getType() == Tipo.tipo.REAL
                || tipoVariable.getType() == Tipo.tipo.BOOLEAN
                || tipoVariable.getType() == Tipo.tipo.CHAR
                || tipoVariable.getType() == Tipo.tipo.WORD) {
            codigo += variable.get4D(tabla, arbol);
            String temp1 = tabla.getTemporalActual();
            if (variable.accesoGlobal) {
                codigo += valor.get4D(tabla, arbol);
                codigo += "=," + temp1 + "," + tabla.getTemporalActual() + ",heap\n";
            } else {
                String temp2 = tabla.getTemporal();
                codigo += valor.get4D(tabla, arbol);
                codigo += "+,p," + temp1 + "," + temp2 + "\n";
                codigo += "=," + temp2 + "," + tabla.getTemporalActual() + ",stack\n";
            }
        } else if (tipoVariable.getType() == Tipo.tipo.STRING) {
            codigo += variable.get4D(tabla, arbol);
            String temp1 = tabla.getTemporalActual();
            if (variable.accesoGlobal) {
                codigo += valor.get4D(tabla, arbol);
                codigo += "=," + temp1 + "," + tabla.getTemporalActual() + ",heap\n";
            } else {
                String temp2 = tabla.getTemporal();
                codigo += valor.get4D(tabla, arbol);
                codigo += "+,p," + temp1 + "," + temp2 + "\n";
                codigo += "=," + temp2 + "," + tabla.getTemporalActual() + ",stack\n";
            }
        } else if (tipoVariable.getType() == Tipo.tipo.RANGE) {
            codigo += variable.get4D(tabla, arbol);
            String temp1 = tabla.getTemporalActual();
            String temp2 = "";
            String temp3 = "";
            String temp4 = "";
            String label1 = tabla.getEtiqueta();
            String label2 = tabla.getEtiqueta();
            String label3 = tabla.getEtiqueta();
            codigo += valor.get4D(tabla, arbol);
            temp2 = tabla.getTemporalActual();

            codigo += tipoVariable.getLowerLimit().get4D(tabla, arbol);
            temp3 = tabla.getTemporalActual();

            codigo += tipoVariable.getUpperLimit().get4D(tabla, arbol);
            temp4 = tabla.getTemporalActual();
            codigo += "jl," + temp2 + "," + temp3 + "," + label1 + "\n"; // Si es menor al limite inferior salir a error
            codigo += "jg," + temp2 + "," + temp4 + "," + label2 + "\n"; // Si es mayor al limite superior salir a error
            if (variable.accesoGlobal) {
                codigo += "=, " + temp1 + ", " + temp2 + ", heap\n";
            } else {
                String temp5 = tabla.getTemporal();
                codigo += valor.get4D(tabla, arbol);
                codigo += "+,p," + temp1 + "," + temp5 + "\n";
                codigo += "=," + temp5 + "," + temp2 + ",stack\n";
            }
            codigo += "jmp,,," + label3 + "\n";
            codigo += label1 + ":\n";
            codigo += label2 + ":\n";
            codigo += "call,,,rango_sobrepasado\n";
            codigo += label3 + ":\n";
        } else if (tipoVariable.getType() == Tipo.tipo.ENUMERADO) {
            codigo += variable.get4D(tabla, arbol);
            String temp1 = tabla.getTemporalActual();
            codigo += valor.get4D(tabla, arbol);
            String temp2 = tabla.getTemporalActual();
            if (variable.accesoGlobal) {
                codigo += "=, " + temp1 + ", " + temp2 + ", heap\n";
            } else {
                String temp3 = tabla.getTemporal();
                codigo += "+,p," + temp1 + "," + temp3 + "\n";
                codigo += "=," + temp3 + "," + temp2 + ",stack\n";
            }
        } else if (tipoVariable.getType() == Tipo.tipo.ARREGLO) {
            codigo += variable.get4D(tabla, arbol);
            String temp1 = tabla.getTemporalActual();
            codigo += valor.get4D(tabla, arbol);
            String temp2 = tabla.getTemporalActual();
            if (variable.accesoGlobal) {
                codigo += "=, " + temp1 + ", " + temp2 + ", heap\n";
            } else {
                String temp3 = tabla.getTemporal();
                codigo += "+,p," + temp1 + "," + temp3 + "\n";
                codigo += "=," + temp3 + "," + temp2 + ",stack\n";
            }
        } else if (tipoVariable.getType() == Tipo.tipo.RECORD) {
            codigo += variable.get4D(tabla, arbol);
            String temp1 = tabla.getTemporalActual();
            codigo += valor.get4D(tabla, arbol);
            String temp2 = tabla.getTemporalActual();
            if (variable.accesoGlobal) {
                codigo += "=, " + temp1 + ", " + temp2 + ", heap\n";
            } else {
                String temp3 = tabla.getTemporal();
                codigo += "+,p," + temp1 + "," + temp3 + "\n";
                codigo += "=," + temp3 + "," + temp2 + ",stack\n";
            }
            if(valor instanceof Malloc) {
                for (int k = 0; k < tipoVariable.getAtributos().size(); k++) {
                    Registro r = tipoVariable.getAtributos().get(k);
                    Tipo tipoAtr = r.getTipo();
                    if (tipoAtr.getType() == Tipo.tipo.ARREGLO) {
                        String temp10 = tabla.getTemporal();
                        String temp20 = tabla.getTemporal();
                        String temp30 = tabla.getTemporal();
                        String temp40 = tabla.getTemporal();
                        String temp50 = tabla.getTemporal();
                        String temp60 = tabla.getTemporal();
                        String temp70 = tabla.getTemporal();
                        String temp80 = tabla.getTemporal();
                        String label10 = tabla.getEtiqueta();
                        String label20 = tabla.getEtiqueta();
                        codigo += "+," + temp2 + "," + k + "," + temp80 + "\n";
                        codigo += "=," + temp80 + ",," + temp10 + "// Inicio declaracion array\n";
                        ArrayList<Dimension> dimension = tipoAtr.getDimensiones();
                        codigo += "=,h,," + temp60 + "\n";
                        codigo += "=,1,," + temp50 + "\n";
                        for (int m = 0; m < dimension.size(); m++) {
                            Dimension dim = dimension.get(m);
                            codigo += dim.getLimiteInferior().get4D(tabla, arbol);
                            codigo += "=," + tabla.getTemporalActual() + ",," + temp20 + "\n";
                            codigo += dim.getLimiteSuperior().get4D(tabla, arbol);
                            codigo += "=," + tabla.getTemporalActual() + ",," + temp30 + "\n";
                            codigo += "-," + temp30 + "," + temp20 + "," + temp40 + "// Tamaño dimension " + m + "\n";
                            codigo += "*," + temp50 + "," + temp40 + "," + temp50 + "\n";
                        }
                        codigo += label10 + ":\n";
                        codigo += "je," + temp50 + ",0," + label20 + "\n";
                        codigo += "-," + temp50 + ",1," + temp50 + "\n";
                        codigo += Tipo.valorPredeterminado(tipoAtr).get4D(tabla, arbol);
                        codigo += "=," + tabla.getTemporalActual() + ",," + temp70 + "\n";
                        codigo += "=,h," + temp70 + ",heap\n";
                        codigo += "+,h,1,h\n";
                        codigo += "jmp,,," + label10 + "\n";
                        codigo += label20 + ":\n";

                        codigo += "=, " + temp10 + ", " + temp60 + ", heap // Fin declaracion Array " + "\n";
                    } else {
                        String temp10 = tabla.getTemporal();
                        codigo += "+," + temp2 + "," + k + "," + temp10 + "\n";
                        codigo += Tipo.valorPredeterminado(tipoAtr).get4D(tabla, arbol);
                        codigo += "=, " + temp10 + ", " + tabla.getTemporalActual() + ", heap\n";
                    }
                }
            }
        }
        codigo += "// Fin asignacion\n";
        return codigo;
    }

    @Override
    public int getEspacios(int espacios) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
