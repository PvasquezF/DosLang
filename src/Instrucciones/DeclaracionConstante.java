/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Instrucciones;

import Excepciones.Excepcion;
import Expresiones.Identificador;
import Expresiones.Primitivo;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.*;

import java.util.ArrayList;

/**
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
            Tipo tipoAux = tipo.verificarUserType(tabla);
            Simbolo simbolo = new Simbolo(identificador, tipoAux, tabla.getAmbito(), "variable", "global", valor, true, tabla.getHeap());
            Object resultTipo = valor.getTipo(tabla, arbol);
            if (tipo.getType() == Tipo.tipo.ENUMERADO) {
            }
            if (resultTipo instanceof Excepcion) {
                return resultTipo;
            }
            char c;
            if ((tipoAux.equals(new Tipo(Tipo.tipo.CHAR))
                    || tipoAux.equals(new Tipo(Tipo.tipo.INTEGER))
                    || tipoAux.equals(new Tipo(Tipo.tipo.REAL))
                    || tipoAux.equals(new Tipo(Tipo.tipo.RANGE)))
                    && ((Tipo) resultTipo).equals(new Tipo(Tipo.tipo.NIL))) {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        "No se puede asignar NIL al tipo " + tipoAux.toString() + ".",
                        fila, columna);
                arbol.getErrores().add(exc);
                return exc;
            }

            Tipo tipoValor = (Tipo) resultTipo;
            if (tipoAux.getType() == Tipo.tipo.RANGE) {
                Object result = tipoAux.getLowerLimit().getTipo(tabla, arbol);
                Object result1 = tipoAux.getUpperLimit().getTipo(tabla, arbol);
                if (result instanceof Excepcion) {
                    return result;
                }
                if (result1 instanceof Excepcion) {
                    return result1;
                }
                Tipo tipoLower = (Tipo) result;
                Tipo tipoUpper = (Tipo) result1;
                if (tipoLower.equals(tipoUpper)) {
                    if (tipoLower.equals(tipoValor)) {
                        simbolo.getTipo().setTipoRange(tipoLower.getType());
                        Object result3 = tabla.InsertarVariable(simbolo);
                        if (result3 != null) {
                            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                                    (String) result3,
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
                } else {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            "Los tipos del limite de rango no coinciden.",
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
            } else if (tipoAux.getType() == Tipo.tipo.ENUMERADO) {
                if (tipo.getType() == Tipo.tipo.ENUMERADO) {
                    ArrayList<Expresion> listaId = tipoAux.getIdentificadores();
                    for (int j = 0; j < listaId.size(); j++) {
                        Expresion m = listaId.get(j);
                        Identificador id = (Identificador) m;
                        Tipo tipoEnum = new Tipo(Tipo.tipo.ENUMERADO, null);
                        tipoEnum.setNombreEnum(identificador);
                        Simbolo simboloEnum = new Simbolo(id.getIdentificador(), tipoEnum, tabla.getAmbito(), identificador + "_Enum_Item", "global", new Primitivo(j), false, tabla.getHeap());
                        Object result = tabla.InsertarVariable(simboloEnum);
                        if (result != null) {
                            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                                    (String) result,
                                    fila, columna);
                            arbol.getErrores().add(exc);
                            return exc;
                        }
                    }
                }
                if (tipoAux.equals(tipoValor)) {
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
                            "El tipo de la variable no coincide con el valor a asignar, "
                                    + "Tipo " + tipoAux.getNombreEnum() + " = "
                                    + "Tipo " + (tipoValor.getNombreEnum() != null ? tipoValor.getNombreEnum() : tipoValor.getType()) + ".",
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
            } else if (tipoAux.getType() == Tipo.tipo.RECORD) {
                if (tipo.getType() == Tipo.tipo.RECORD) {
                    simbolo.getTipo().setTipoObjeto(identificador);
                }
                ArrayList<Registro> registros = tipoAux.getAtributos();
                int apariciones = 0;
                for (Registro reg : registros) {
                    apariciones = 0;
                    for (Registro r : registros) {
                        if (reg.getIdentificador().equalsIgnoreCase(r.getIdentificador())) {
                            apariciones++;
                        }
                    }
                    if (apariciones > 1) {
                        Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                                "El atributo " + reg.getIdentificador() + " esta duplicado.",
                                fila, columna);
                        arbol.getErrores().add(exc);
                        return exc;
                    }
                }
            } else {
                if (tipoAux.equals(tipoValor)) {
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
                Simbolo sim = (Simbolo) result;
                Tipo tipoAux = tipo.verificarUserType(tabla);
                if (tipoAux.getType() == Tipo.tipo.RANGE) {
                    String temp1 = tabla.getTemporal();
                    String temp2 = "";
                    String temp3 = "";
                    String temp4 = "";
                    String label1 = tabla.getEtiqueta();
                    String label2 = tabla.getEtiqueta();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "\n";
                    codigo += sim.getValor().get4D(tabla, arbol);
                    temp2 = tabla.getTemporalActual();

                    codigo += tipoAux.getLowerLimit().get4D(tabla, arbol);
                    temp3 = tabla.getTemporalActual();

                    codigo += tipoAux.getUpperLimit().get4D(tabla, arbol);
                    temp4 = tabla.getTemporalActual();
                    codigo += "jl," + temp2 + "," + temp3 + "," + label1 + "\n"; // Si es menor al limite inferior salir a error
                    codigo += "jg," + temp2 + "," + temp4 + "," + label2 + "\n"; // Si es mayor al limite superior salir a error
                    codigo += "=, " + temp1 + ", " + temp2 + ", heap\n";
                    codigo += label1 + ":\n";
                    codigo += label2 + ":\n";
                } else if (tipoAux.getType() == Tipo.tipo.ENUMERADO) {
                    if (tipo.getType() == Tipo.tipo.ENUMERADO) {
                        for (int j = 0; j < tipo.getIdentificadores().size(); j++) {
                            //Tipo t = tabla.getListaTipos().get(i).getTipo();
                            Identificador identificadorEnum = (Identificador) tipo.getIdentificadores().get(j);
                            Object resultEnum = tabla.getVariable(identificadorEnum.getIdentificador());
                            if (resultEnum instanceof String) {
                                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                                        (String) resultEnum,
                                        fila, columna);
                                arbol.getErrores().add(exc);
                                return exc;
                            } else {
                                Simbolo simEnum = (Simbolo) resultEnum;
                                String temp1 = tabla.getTemporal();
                                codigo += "=," + simEnum.getApuntador() + ",," + temp1 + "\n";
                                codigo += simEnum.getValor().get4D(tabla, arbol);
                                codigo += "=, " + temp1 + ", " + tabla.getTemporalActual() + ", heap\n";
                            }
                        }
                    }
                    String temp1 = tabla.getTemporal();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "\n";
                    codigo += sim.getValor().get4D(tabla, arbol);
                    codigo += "=, " + temp1 + ", " + tabla.getTemporalActual() + ", heap\n";
                } else if (tipoAux.getType() == Tipo.tipo.ARREGLO) {
                    String temp1 = tabla.getTemporal();
                    String temp2 = tabla.getTemporal();
                    String temp3 = tabla.getTemporal();
                    String temp4 = tabla.getTemporal();
                    String temp5 = tabla.getTemporal();
                    String temp6 = tabla.getTemporal();
                    String temp7 = tabla.getTemporal();
                    String label1 = tabla.getEtiqueta();
                    String label2 = tabla.getEtiqueta();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "\n";
                    ArrayList<Dimension> dimension = tipoAux.getDimensiones();
                    codigo += "=,h," + dimension.size() + ",heap // Cantidad dimensiones arreglo\n";
                    codigo += "=,h,," + temp6 + "\n";
                    codigo += "+,h,1,h\n";
                    codigo += "=,1,," + temp5 + "\n";
                    for (int k = 0; k < dimension.size(); k++) {
                        Dimension dim = dimension.get(k);
                        codigo += dim.getLimiteInferior().get4D(tabla, arbol);
                        codigo += "=," + tabla.getTemporalActual() + ",," + temp2 + "\n";
                        codigo += dim.getLimiteSuperior().get4D(tabla, arbol);
                        codigo += "=," + tabla.getTemporalActual() + ",," + temp3 + "\n";
                        codigo += "-," + temp3 + "," + temp2 + "," + temp4 + "// Tamaño dimension " + k + "\n";
                        codigo += "=,h," + temp4 + ",heap // dimension " + k + "\n";
                        codigo += "+,h,1,h\n";
                        codigo += "*," + temp5 + "," + temp4 + "," + temp5 + "\n";
                    }
                    codigo += label1 + ":\n";
                    codigo += "je," + temp5 + ",0," + label2 + "\n";
                    codigo += "-," + temp5 + ",1," + temp5 + "\n";
                    codigo += Tipo.valorPredeterminado(tipoAux).get4D(tabla, arbol);
                    codigo += "=," + tabla.getTemporalActual() + ",," + temp7 + "\n";
                    codigo += "=,h," + temp7 + ",heap\n";
                    codigo += "+,h,1,h\n";
                    codigo += "jmp,,," + label1 + "\n";
                    codigo += label2 + ":\n";

                    codigo += "=, " + temp1 + ", " + temp6 + ", heap // Fin declaracion Array " + identificador + "\n";
                } else if (tipoAux.getType() == Tipo.tipo.RECORD) {
                    /*String temp1 = tabla.getTemporal();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "// Inicio declaracion objeto\n";
                    codigo += "=," + temp1 + ",h,heap\n";
                    for (int k = 0; k < tipoAux.getAtributos().size(); k++) {
                        Registro r = tipoAux.getAtributos().get(k);
                        codigo += Tipo.valorPredeterminado(r.getTipo()).get4D(tabla, arbol);
                        codigo += "=,h," + tabla.getTemporalActual() + ",heap\n";
                        codigo += "+,h,1,h\n";
                    }*/
                    String temp1 = tabla.getTemporal();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "// Inicio declaracion objeto\n";
                    codigo += "=," + temp1 + ",-1,heap\n";
                }  else {
                    String temp1 = tabla.getTemporal();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "\n";
                    codigo += sim.getValor().get4D(tabla, arbol);
                    codigo += "=, " + temp1 + ", " + tabla.getTemporalActual() + ", heap\n";
                }
            }
        }
        return codigo;
    }

    @Override
    public int getEspacios(int espacios) {
//        if (tipo.getType() == Tipo.tipo.ENUMERADO){
//            espacios += tipo.getIdentificadores().size();
//        }
        return espacios += identificadores.size();
    }
}
