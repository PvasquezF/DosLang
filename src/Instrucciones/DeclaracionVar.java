/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Instrucciones;

import Excepciones.Excepcion;
import Expresiones.Acceso;
import Expresiones.Identificador;
import Expresiones.Primitivo;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.*;

import java.util.ArrayList;

/**
 * @author Pavel
 */
public class DeclaracionVar implements Instruccion {

    private ArrayList<String> identificadores;
    private Tipo tipo;
    private Expresion valor;
    private boolean stack;
    private int fila;
    private int columna;

    public DeclaracionVar(Tipo tipo, ArrayList<String> identificadores, int fila, int columna) {
        this.tipo = tipo;
        this.identificadores = identificadores;
        this.fila = fila;
        this.columna = columna;
    }

    public DeclaracionVar(Tipo tipo, ArrayList<String> identificadores, Expresion valor, int fila, int columna) {
        this.tipo = tipo;
        this.identificadores = identificadores;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        if (valor == null) {
            for (int i = 0; i < identificadores.size(); i++) {
                String identificador = identificadores.get(i);
                Tipo tipoAux = tipo.verificarUserType(tabla);
                Expresion resultTipo = Tipo.valorPredeterminado(tipoAux);
                Simbolo simbolo = null;
                if (stack) {
                    simbolo = new Simbolo(identificador, tipoAux, tabla.getAmbito(), "variable", "local", resultTipo, false, tabla.getEnviroment().getPosicionStack());
                } else {
                    simbolo = new Simbolo(identificador, tipoAux, tabla.getAmbito(), "variable", "global", resultTipo, false, tabla.getHeap());
                }
                if (tipoAux.getType() == Tipo.tipo.RANGE) {
                    Object result = tipoAux.getLowerLimit().getTipo(tabla, arbol);
                    if (result instanceof Excepcion) {
                        return result;
                    }
                    Object result1 = tipoAux.getUpperLimit().getTipo(tabla, arbol);
                    if (result1 instanceof Excepcion) {
                        return result1;
                    }
                    Tipo tipoLower = (Tipo) result;
                    Tipo tipoUpper = (Tipo) result1;
                    if (tipoLower.equals(tipoUpper)) {
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
                            Acceso acceso = (Acceso) m;
                            Identificador id = (Identificador) acceso.getAccesos().get(0);
                            //Tipo tipoEnum = new Tipo(Tipo.tipo.ENUMERADO, null);
                            //tipoEnum.setNombreEnum(identificador);
                            Simbolo simboloEnum;
                            if (stack) {
                                simboloEnum = new Simbolo(id.getIdentificador(), tipoAux, tabla.getAmbito(), identificador + "_Enum_Item", "local", new Primitivo(j), false, tabla.getEnviroment().getPosicionStack());
                            } else {
                                simboloEnum = new Simbolo(id.getIdentificador(), tipoAux, tabla.getAmbito(), identificador + "_Enum_Item", "global", new Primitivo(j), false, tabla.getHeap());
                            }
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
                }
                Object result = tabla.InsertarVariable(simbolo);
                if (result != null) {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            (String) result,
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
            }
        } else {
            for (int i = 0; i < identificadores.size(); i++) {
                String identificador = identificadores.get(i);
                Tipo tipoAux = tipo.verificarUserType(tabla);
                Simbolo simbolo = null;
                if (stack) {
                    simbolo = new Simbolo(identificador, tipoAux, tabla.getAmbito(), "variable", "local", valor, false, tabla.getEnviroment().getPosicionStack());
                } else {
                    simbolo = new Simbolo(identificador, tipoAux, tabla.getAmbito(), "variable", "global", valor, false, tabla.getHeap());
                }
                Object resultTipo = valor.getTipo(tabla, arbol);
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
                    if (result instanceof Excepcion) {
                        return result;
                    }
                    Object result1 = tipoAux.getUpperLimit().getTipo(tabla, arbol);
                    if (result1 instanceof Excepcion) {
                        return result1;
                    }
                    Tipo tipoLower = (Tipo) result;
                    Tipo tipoUpper = (Tipo) result1;
                    if (tipoLower.equals(tipoUpper)) {
                        if (tipoLower.equals(tipoValor)) {
                            simbolo.getTipo().setTipoRange(tipoValor.getType());
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
                            Acceso acceso = (Acceso) m;
                            Identificador id = (Identificador) acceso.getAccesos().get(0);
                            //Tipo tipoEnum = new Tipo(Tipo.tipo.ENUMERADO, null);
                            //tipoEnum.setNombreEnum(identificador);
                            Simbolo simboloEnum = null;
                            if (stack) {
                                simboloEnum = new Simbolo(id.getIdentificador(), tipoAux, tabla.getAmbito(), identificador + "_Enum_Item", "local", new Primitivo(j), false, tabla.getEnviroment().getPosicionStack());
                            } else {
                                simboloEnum = new Simbolo(id.getIdentificador(), tipoAux, tabla.getAmbito(), identificador + "_Enum_Item", "global", new Primitivo(j), false, tabla.getHeap());
                            }
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
        }
        return null;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Inicio declaracion var linea: " + fila + ", columna: " + columna + "\n";
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
                    String label3 = tabla.getEtiqueta();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "\n";
                    codigo += sim.getValor().get4D(tabla, arbol);
                    temp2 = tabla.getTemporalActual();

                    codigo += tipoAux.getLowerLimit().get4D(tabla, arbol);
                    temp3 = tabla.getTemporalActual();

                    codigo += tipoAux.getUpperLimit().get4D(tabla, arbol);
                    temp4 = tabla.getTemporalActual();
                    codigo += "jl," + temp2 + "," + temp3 + "," + label1 + "\n"; // Si es menor al limite inferior salir a error
                    codigo += "jg," + temp2 + "," + temp4 + "," + label2 + "\n"; // Si es mayor al limite superior salir a error
                    if (isStack()) {
                        String temp5 = tabla.getTemporal();
                        codigo += "+,p," + temp1 + "," + temp5 + "\n";
                        codigo += "=," + temp5 + "," + temp2 + ",stack\n";
                    } else {
                        codigo += "=, " + temp1 + ", " + temp2 + ", heap\n";
                    }
                    codigo += "jmp,,," + label3 + "\n";
                    codigo += label1 + ":\n";
                    codigo += label2 + ":\n";
                    codigo += "call,,,rango_sobrepasado\n";
                    codigo += label3 + ":\n";
                } else if (tipoAux.getType() == Tipo.tipo.ENUMERADO) {
                    if (tipo.getType() == Tipo.tipo.ENUMERADO) {
                        for (int j = 0; j < tipo.getIdentificadores().size(); j++) {
                            //Tipo t = tabla.getListaTipos().get(i).getTipo();
                            Acceso acceso = (Acceso) tipo.getIdentificadores().get(j);
                            Identificador identificadorEnum = (Identificador) acceso.getAccesos().get(0);
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
                                String temp5 = tabla.getTemporal();
                                codigo += "=," + simEnum.getApuntador() + ",," + temp1 + "\n";
                                codigo += simEnum.getValor().get4D(tabla, arbol);
                                codigo += "=," + tabla.getTemporalActual() + ",," + temp5 + "\n";
                                if (isStack()) {
                                    String temp6 = tabla.getTemporal();
                                    codigo += "+,p," + temp1 + "," + temp6 + "\n";
                                    codigo += "=, " + temp6 + ", " + temp5 + ", stack\n";
                                } else {
                                    codigo += "=, " + temp1 + ", " + temp5 + ", heap\n";
                                }
                            }
                        }
                    }
                    String temp1 = tabla.getTemporal();
                    String temp2 = tabla.getTemporal();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "\n";
                    codigo += sim.getValor().get4D(tabla, arbol);
                    codigo += "=," + tabla.getTemporalActual() + ",," + temp2 + "\n";
                    if (isStack()) {
                        String temp6 = tabla.getTemporal();
                        codigo += "+,p," + temp1 + "," + temp6 + "\n";
                        codigo += "=, " + temp6 + ", " + temp2 + ", stack\n";
                    } else {
                        codigo += "=, " + temp1 + ", " + temp2 + ", heap\n";
                    }
                } else if (tipoAux.getType() == Tipo.tipo.ARREGLO) {
                    String temp1 = tabla.getTemporal();
                    String temp6 = tabla.getTemporal();
                    String tempCont0 = tabla.getTemporal();
                    String tempCont1 = tabla.getTemporal();
                    String tempCont2 = tabla.getTemporal();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "// Inicio declaracion array\n";
                    ArrayList<Dimension> dimension = tipoAux.getDimensiones();
                    codigo += "=,h,," + temp6 + "\n";
                    codigo += "=,1,," + tempCont0 + "\n";
                    codigo += "=,1,," + tempCont1 + "\n";
                    ArrayList<String> posicionesArray = new ArrayList<>();
                    for (int k = 0; k < dimension.size(); k++) {
                        String temp2 = tabla.getTemporal();
                        String temp3 = tabla.getTemporal();
                        String temp4 = tabla.getTemporal();
                        Dimension dim = dimension.get(k);
                        codigo += dim.getLimiteInferior().get4D(tabla, arbol);
                        codigo += "=," + tabla.getTemporalActual() + ",," + temp2 + "\n";
                        codigo += dim.getLimiteSuperior().get4D(tabla, arbol);
                        codigo += "=," + tabla.getTemporalActual() + ",," + temp3 + "\n";

                        codigo += "-," + temp3 + "," + temp2 + "," + temp4 + "// Tamaño dimension " + k + "\n";
                        codigo += "+," + temp4 + ",2," + temp4 + "\n";
                        if (posicionesArray.indexOf(temp4) == -1) {
                            posicionesArray.add(temp4);
                        }
                    }
                    if (posicionesArray.size() > 0) {
                        codigo += "=," + posicionesArray.get(0) + ",," + tempCont2 + "\n";
                    }
                    String temp9 = tabla.getTemporal();
                    String temp14 = tabla.getTemporal();
                    codigo += "=,0,," + temp14 + "\n";
                    for (int k = 0; k < dimension.size(); k++) {
                        String temp2 = tabla.getTemporal();
                        String temp3 = tabla.getTemporal();
                        String temp4 = tabla.getTemporal();
                        String temp5 = tabla.getTemporal();
                        String temp7 = tabla.getTemporal();
                        String temp8 = tabla.getTemporal();
                        String temp10 = tabla.getTemporal();
                        String temp11 = tabla.getTemporal();
                        String temp12 = tabla.getTemporal();
                        String temp13 = tabla.getTemporal();
                        String label1 = tabla.getEtiqueta();
                        String label2 = tabla.getEtiqueta();
                        String labelCont0 = tabla.getEtiqueta();
                        String labelCont1 = tabla.getEtiqueta();
                        Dimension dim = dimension.get(k);

                        codigo += labelCont0 + ":\n";
                        codigo += "je," + tempCont0 + ",0," + labelCont1 + "\n";
                        codigo += dim.getLimiteInferior().get4D(tabla, arbol);
                        codigo += "=," + tabla.getTemporalActual() + ",," + temp2 + "\n";
                        codigo += dim.getLimiteSuperior().get4D(tabla, arbol);
                        codigo += "=," + tabla.getTemporalActual() + ",," + temp3 + "\n";

                        if (k == 0) {
                            codigo += "=,h,," + temp9 + "\n";
                        }
                        codigo += "=,h," + temp2 + ",heap // Dimension " + k + ", lim Sup\n";
                        codigo += "+,h,1,h\n";
                        codigo += "=,h," + temp3 + ",heap // Dimension " + k + ", lim Inf\n";
                        codigo += "+,h,1,h\n";

                        codigo += "-," + temp3 + "," + temp2 + "," + temp4 + "// Tamaño dimension " + k + "\n";
                        codigo += "=," + temp4 + ",," + temp8 + "\n";

                        codigo += label1 + ":\n";
                        codigo += "je," + temp4 + ",0," + label2 + "\n";
                        codigo += "=,h,," + temp5 + "\n";
                        if (k + 1 == dimension.size()) {
                            codigo += Tipo.valorPredeterminado(tipoAux).get4D(tabla, arbol);
                            codigo += "=,h," + tabla.getTemporalActual() + ",heap\n";
                            codigo += "=,0,," + temp13 + "\n";
                        } else {
                            codigo += "+," + temp9 + "," + posicionesArray.get(k) + "," + temp10 + "\n";
                            //codigo += "-," + temp8 + "," + temp4 + "," + temp11 + "\n";
                            //codigo += "-," + temp11 + ",1," + temp11 + "\n";
                            codigo += "*," + posicionesArray.get(k + 1) + "," + temp14 + "," + temp12 + "\n";
                            codigo += "+," + temp14 + ",1," + temp14 + "\n";
                            codigo += "+," + temp10 + "," + temp12 + "," + temp13 + "\n";
                            codigo += "=,h," + temp13 + ",heap\n";
                        }
                        codigo += "-," + temp4 + ",1," + temp4 + "\n";
                        codigo += "+,h,1,h\n";
                        codigo += "jmp,,," + label1 + "\n";
                        codigo += label2 + ":\n";
                        codigo += "-," + tempCont0 + ",1," + tempCont0 + "\n";
                        codigo += "jmp,,," + labelCont0 + "\n";
                        codigo += labelCont1 + ":\n";
                        codigo += "=," + temp13 + ",," + temp9 + "\n";
                        codigo += "=,0,," + temp14 + "\n";
                        codigo += "-," + temp3 + "," + temp2 + "," + temp7 + "\n";
                        codigo += "*," + tempCont1 + "," + temp7 + "," + tempCont1 + "// Actualizando contador\n";
                        codigo += "=," + tempCont1 + ",," + tempCont0 + "\n";

                    }

                    if (isStack()) {
                        String temp8 = tabla.getTemporal();
                        codigo += "+,p," + temp1 + "," + temp8 + "\n";
                        codigo += "=, " + temp8 + ", " + temp6 + ", stack// Fin declaracion Array " + identificador + "\n";
                    } else {
                        //codigo += "=, " + temp1 + ", " + temp2 + ", heap\n";
                        codigo += "=, " + temp1 + ", " + temp6 + ", heap // Fin declaracion Array " + identificador + "\n";
                    }


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
                    if (isStack()) {
                        String temp2 = tabla.getTemporal();
                        codigo += "+,p," + temp1 + "," + temp2 + "\n";
                        codigo += "=, " + temp2 + ", -1, stack\n";
                    } else {
                        codigo += "=," + temp1 + ",-1,heap\n";
                    }
                } else if (tipoAux.getType() == Tipo.tipo.STRING) {
                    String temp1 = tabla.getTemporal();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "\n";
                    if (valor == null) {
                        //codigo += "=," + sim.getApuntadorRef() + ",," + temp2 + "\n";
                        //codigo += sim.getValor().get4D(tabla, arbol);
                        //codigo += "=, " + temp2 + ", " + tabla.getTemporalActual() + ", heap\n";
                        if (isStack()) {
                            String temp2 = tabla.getTemporal();
                            codigo += "+,p," + temp1 + "," + temp2 + "\n";
                            codigo += "=, " + temp2 + ", -1, stack\n";
                        } else {
                            codigo += "=," + temp1 + ",-1,heap\n";
                        }
                    } else {
                        if (isStack()) {
                            String temp2 = tabla.getTemporal();
                            codigo += "+,p," + temp1 + "," + temp2 + "\n";
                            codigo += sim.getValor().get4D(tabla, arbol);
                            codigo += "=, " + temp2 + ", " + tabla.getTemporalActual() + ", stack\n";
                        } else {
                            codigo += sim.getValor().get4D(tabla, arbol);
                            codigo += "=, " + temp1 + ", " + tabla.getTemporalActual() + ", heap\n";
                        }
                    }
                } else {
                    String temp1 = tabla.getTemporal();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "\n";
                    if (stack) {
                        String temp2 = tabla.getTemporal();
                        codigo += "+,p," + temp1 + "," + temp2 + "\n";
                        codigo += sim.getValor().get4D(tabla, arbol);
                        codigo += "=, " + temp2 + ", " + tabla.getTemporalActual() + ", stack\n";
                    } else {
                        codigo += sim.getValor().get4D(tabla, arbol);
                        codigo += "=, " + temp1 + ", " + tabla.getTemporalActual() + ", heap\n";
                    }
                }
            }
        }
        codigo += "// Fin declaracion var\n";
        return codigo;
    }

    @Override
    public int getEspacios(int espacios) {
        if (tipo.getType() == Tipo.tipo.ENUMERADO) {
            espacios += tipo.getIdentificadores().size();
        }
        return espacios += identificadores.size();
    }

    public boolean isStack() {
        return stack;
    }

    public void setStack(boolean stack) {
        this.stack = stack;
    }

    public ArrayList<String> getIdentificadores() {
        return identificadores;
    }

    public void setIdentificadores(ArrayList<String> identificadores) {
        this.identificadores = identificadores;
    }
}
