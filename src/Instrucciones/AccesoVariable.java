package Instrucciones;

import Excepciones.Excepcion;
import Expresiones.AccesoArreglo;
import Expresiones.Identificador;
import Interfaces.Expresion;
import TablaSimbolos.*;

import java.util.ArrayList;

public class AccesoVariable implements Expresion {
    private ArrayList<Expresion> accesos;
    public boolean accesoGlobal = false;
    private int fila;
    private int columna;

    public AccesoVariable(ArrayList<Expresion> accesos, int fila, int columna) {
        this.accesos = accesos;
        this.fila = fila;
        this.columna = columna;
    }

    // a[2][4][1]
    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        Tipo tipoResultado = null;
        Identificador identificador = (Identificador) accesos.get(0);
        Object result = tabla.getVariable(identificador.getIdentificador());
        if (result instanceof String) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    (String) result,
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        identificador.getTipo(tabla, arbol);
        Simbolo sim = (Simbolo) result;
        accesoGlobal = ((Simbolo) sim).getNivel().equalsIgnoreCase("global");
        tipoResultado = sim.getTipo();
        for (int i = 1; i < accesos.size(); i++) {
            Expresion acceso = accesos.get(i);
            if (tipoResultado.getType() == Tipo.tipo.ARREGLO) {
                if (acceso instanceof AccesoArreglo) {
                    int cantidadDimensiones = tipoResultado.getDimensiones().size();
                    int cantidadAccesos = ((AccesoArreglo) acceso).getIndices().size();
                    if (cantidadAccesos == cantidadDimensiones) {
                        if (tipoResultado.getTipoArreglo() == Tipo.tipo.RECORD) {
                            Tipo tipoArAtr = new Tipo(tipoResultado.getTipoArreglo(), tipoResultado.getTipoObjeto());
                            tipoArAtr.setAtributos(tipoResultado.getAtributos());
                            tipoResultado = tipoArAtr;
                        } else {
                            tipoResultado = new Tipo(tipoResultado.getTipoArreglo(), tipoResultado.getTipoObjeto());
                        }
                        //tipoResultado = tipoResultado.verificarUserType(tabla);
                    } else if (cantidadAccesos < cantidadDimensiones) {
                        Tipo tipoRet = new Tipo(Tipo.tipo.ARREGLO);
                        tipoRet.setTipoArreglo(tipoResultado.getTipoArreglo());
                        tipoRet.setTipoObjeto(tipoResultado.getTipoObjeto());
                        ArrayList<Dimension> dims = new ArrayList<>();
                        for (int j = cantidadAccesos; j < cantidadDimensiones; j++) {
                            dims.add(tipoResultado.getDimensiones().get(j));
                        }
                        tipoRet.setDimensiones(dims);
                    } else {
                        if (cantidadAccesos < cantidadDimensiones) {
                            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                                    "La cantidad de dimensiones accesadas es menor a la que contiene el arreglo.",
                                    fila, columna);
                            arbol.getErrores().add(exc);
                            return exc;
                        } else {
                            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                                    "La cantidad de dimensiones accesadas es mayor a la que contiene el arreglo.",
                                    fila, columna);
                            arbol.getErrores().add(exc);
                            return exc;
                        }
                    }
                } else {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            "El tipo de acceso que se intenta no es de tipo arreglo.",
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
            } else if (tipoResultado.getType() == Tipo.tipo.RECORD) {
                if (acceso instanceof Identificador) {
                    Identificador atributo = (Identificador) acceso;
                    boolean encontrado = false;
                    for (int j = 0; j < tipoResultado.getAtributos().size(); j++) {
                        Registro registro = tipoResultado.getAtributos().get(j);
                        if (registro.getIdentificador().equalsIgnoreCase(atributo.getIdentificador())) {
                            tipoResultado = registro.getTipo();
                            encontrado = true;
                            break;
                        }
                    }
                    if (!encontrado) {
                        Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                                "No existe el atributo " + atributo.getIdentificador(),
                                fila, columna);
                        arbol.getErrores().add(exc);
                        return exc;
                    }
                } else {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            "El tipo de acceso que se intenta no es de tipo registro.",
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
            } else {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        "AccessException, No se puede acceder a una variable de tipo " + tipoResultado.getType() + ".",
                        fila, columna);
                arbol.getErrores().add(exc);
                return exc;
            }
        }
        return tipoResultado;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        // Bloque para variables with
        Identificador identificadorAux = (Identificador) accesos.get(0);
        Simbolo simAux = (Simbolo) tabla.getVariable(identificadorAux.getIdentificador());
        if (simAux != null && simAux.isWith()) {
            ArrayList<Expresion> listaNueva = new ArrayList<>();
            for (Expresion e : simAux.getAccesoVariable().getAccesos()) {
                listaNueva.add(e);
            }
            listaNueva.addAll(this.getAccesos());
            this.setAccesos(listaNueva);
        }
        // Fin Bloque para variables with

        String codigo = "// Inicio AccesoVariable linea: " + fila + ", columna: " + columna + "\n";
        String tagNullPointer = tabla.getEtiqueta();
        Tipo tipoResultado = null;
        String temp7 = tabla.getTemporal();
        Identificador identificador = (Identificador) accesos.get(0);
        codigo += identificador.get4D(tabla, arbol);
        codigo += "=," + tabla.getTemporalActual() + ",," + temp7 + "\n";
        tabla.AgregarTemporal(temp7);
        tabla.QuitarTemporal(tabla.getTemporalActual());
        Object result = tabla.getVariable(identificador.getIdentificador());
        if (result instanceof String) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    (String) result,
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        Simbolo sim = (Simbolo) result;
        tipoResultado = sim.getTipo();
        if (!identificador.accesoGlobal && accesos.size() > 1) {
            String temp9 = tabla.getTemporal();
            codigo += "+,p," + temp7 + "," + temp9 + "\n";
            tabla.AgregarTemporal(temp9);
            tabla.QuitarTemporal(temp7);
            codigo += "=,stack," + temp9 + "," + temp7 + "\n";
            tabla.AgregarTemporal(temp7);
            tabla.QuitarTemporal(temp9);
        } else if (identificador.accesoGlobal && accesos.size() > 1) {
            codigo += "=,heap," + temp7 + "," + temp7 + "\n";
            tabla.AgregarTemporal(temp7);
        }
        //tipoResultado = sim.getTipo().verificarUserType(tabla);
        for (int i = 1; i < accesos.size(); i++) {
            codigo += "je," + temp7 + ",-1," + tagNullPointer + "\n";
            Expresion acceso = accesos.get(i);
            if (tipoResultado.getType() == Tipo.tipo.ARREGLO) {
                if (acceso instanceof AccesoArreglo) {
                    int cantidadDimensiones = tipoResultado.getDimensiones().size(); // [2..4][5..8]
                    int cantidadAccesos = ((AccesoArreglo) acceso).getIndices().size(); // [2,7]
                    if (cantidadAccesos <= cantidadDimensiones) {
                        String temp1 = tabla.getTemporal();
                        String temp2 = tabla.getTemporal();
                        String temp3 = tabla.getTemporal();
                        String temp4 = tabla.getTemporal();
                        String temp5 = tabla.getTemporal();
                        String temp6 = tabla.getTemporal();
                        tabla.AgregarTemporal(temp1);
                        tabla.AgregarTemporal(temp2);
                        tabla.AgregarTemporal(temp3);
                        tabla.AgregarTemporal(temp4);
                        tabla.AgregarTemporal(temp5);
                        tabla.AgregarTemporal(temp6);
                        String label1 = tabla.getEtiqueta();
                        String label2 = tabla.getEtiqueta();
                        String label3 = tabla.getEtiqueta();
                        for (int j = 0; j < cantidadAccesos; j++) {
                            codigo += "+," + temp7 + ",0," + temp2 + "\n";
                            tabla.QuitarTemporal(temp7);
                            tabla.AgregarTemporal(temp2);
                            codigo += "=,heap," + temp2 + "," + temp1 + " // lim inf dim" + j + "\n";
                            tabla.AgregarTemporal(temp1);
                            tabla.QuitarTemporal(temp2);
                            codigo += "+," + temp7 + ",1," + temp3 + "\n";
                            tabla.AgregarTemporal(temp3);
                            tabla.QuitarTemporal(temp7);
                            codigo += "=,heap," + temp3 + "," + temp4 + " // lim sup dim" + j + "\n";
                            tabla.QuitarTemporal(temp3);
                            tabla.AgregarTemporal(temp4);
                            codigo += ((AccesoArreglo) acceso).getIndices().get(j).get4D(tabla, arbol);
                            codigo += "=," + tabla.getTemporalActual() + ",," + temp5 + "\n";
                            tabla.QuitarTemporal(tabla.getTemporalActual());
                            tabla.AgregarTemporal(temp5);
                            codigo += "jl," + temp5 + "," + temp1 + "," + label1 + "\n";
                            tabla.QuitarTemporal(temp5);
                            tabla.QuitarTemporal(temp1);
                            codigo += "jge," + temp5 + "," + temp4 + "," + label2 + "\n";
                            tabla.QuitarTemporal(temp5);
                            tabla.QuitarTemporal(temp4);

                            codigo += "+," + temp7 + ",2," + temp6 + "\n";
                            tabla.QuitarTemporal(temp7);
                            tabla.AgregarTemporal(temp6);
                            codigo += "-," + temp5 + "," + temp1 + "," + temp7 + "\n";
                            tabla.QuitarTemporal(temp5);
                            tabla.QuitarTemporal(temp1);
                            tabla.AgregarTemporal(temp7);
                            codigo += "+," + temp6 + "," + temp7 + "," + temp7 + "\n";
                            tabla.QuitarTemporal(temp6);
                            tabla.AgregarTemporal(temp7);
                            if (j + 1 != cantidadDimensiones) {
                                codigo += "=,heap," + temp7 + "," + temp7 + "\n";
                                tabla.AgregarTemporal(temp7);
                            }
                        }
                        codigo += "jmp,,," + label3 + "\n";
                        codigo += label1 + ":\n";
                        codigo += label2 + ":\n";
                        codigo += "call,,,indexOutException_primitiva\n";
                        codigo += label3 + ":\n";
                        accesoGlobal = true;
                    } else {
                        Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                                "La cantidad de dimensiones accesadas es mayor a la que contiene el arreglo.",
                                fila, columna);
                        arbol.getErrores().add(exc);
                        return exc;
                    }
                    if (tipoResultado.getTipoArreglo() == Tipo.tipo.RECORD) {
                        Tipo tipoArAtr = new Tipo(tipoResultado.getTipoArreglo(), tipoResultado.getTipoObjeto());
                        tipoArAtr.setAtributos(tipoResultado.getAtributos());
                        tipoResultado = tipoArAtr;
                        tipoResultado = tipoResultado.verificarUserType(tabla);
                    } else {
                        tipoResultado = new Tipo(tipoResultado.getTipoArreglo(), tipoResultado.getTipoObjeto());
                        tipoResultado = tipoResultado.verificarUserType(tabla);
                    }
                } else {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            "El tipo de acceso que se intenta no es de tipo arreglo.",
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
            } else if (tipoResultado.getType() == Tipo.tipo.RECORD) {
                Identificador atributo = (Identificador) acceso;
                for (int j = 0; j < tipoResultado.getAtributos().size(); j++) {
                    Registro registro = tipoResultado.getAtributos().get(j);
                    if (registro.getIdentificador().equalsIgnoreCase(atributo.getIdentificador())) {
                        //codigo += atributo.get4D(tabla, arbol);
                        //String temp1 = tabla.getTemporal();
                        /*if (identificador.accesoGlobal) {
                            codigo += "=,heap," + temp7 + "," + temp1 + "\n";
                            codigo += "+," + temp1 + "," + j + "," + temp7 + "\n";
                        } else {
                        }*/
                        codigo += "+," + temp7 + "," + j + "," + temp7 + "\n";
                        tabla.AgregarTemporal(temp7);
                        tipoResultado = registro.getTipo();
                        accesoGlobal = true;
                        break;
                    }
                }
            } else {
                if (acceso instanceof AccesoArreglo) {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            "AccessException, No se puede acceder a una variable de tipo " + tipoResultado.getType() + ".",
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
            }
            if (i + 1 != accesos.size()) {
                codigo += "=,heap," + temp7 + "," + temp7 + "\n";
                tabla.AgregarTemporal(temp7);
            }
        }
        String tagSalida = tabla.getEtiqueta();
        codigo += "jmp,,," + tagSalida + "\n";
        codigo += tagNullPointer + ":\n";
        codigo += "call,,,NullPointerPrimitiva\n";
        codigo += tagSalida + ":\n";
        codigo += "=," + temp7 + ",," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(temp7);
        codigo += "//Fin acceso variable\n";
        return codigo;
    }

    public ArrayList<Expresion> getAccesos() {
        return accesos;
    }

    public void setAccesos(ArrayList<Expresion> accesos) {
        this.accesos = accesos;
    }
}