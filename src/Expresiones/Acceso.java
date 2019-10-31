package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.*;

import java.util.ArrayList;

public class Acceso implements Expresion {
    private ArrayList<Expresion> accesos;
    public boolean accesoGlobal = false;
    private int fila;
    private int columna;

    public Acceso(ArrayList<Expresion> accesos, int fila, int columna) {
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
        Simbolo sim = (Simbolo) result;
        accesoGlobal = ((Simbolo) sim).getNivel().equalsIgnoreCase("global");
        tipoResultado = sim.getTipo();
        //tipoResultado = sim.getTipo().verificarUserType(tabla);
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
                            tipoResultado = tipoResultado.verificarUserType(tabla);
                        } else {
                            tipoResultado = new Tipo(tipoResultado.getTipoArreglo(), tipoResultado.getTipoObjeto());
                            tipoResultado = tipoResultado.verificarUserType(tabla);
                        }
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
                if (acceso instanceof AccesoArreglo) {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            "AccessException, No se puede acceder a una variable de tipo " + tipoResultado.getType() + ".",
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
            }
        }

        return tipoResultado;
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        return null;
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
            Identificador iden = (Identificador) listaNueva.get(0);
            Identificador NuevoIden = new Identificador(iden.getIdentificador(), true, iden.getFila(), iden.getColumna());
            listaNueva.set(0, NuevoIden);
            this.setAccesos(listaNueva);
        }
        // Fin Bloque para variables with

        String codigo = "// Inicio acceso linea: " + fila + ", columna: " + columna + "\n";
        Tipo tipoResultado = null;
        String temp7 = tabla.getTemporal();
        //String temp9 = tabla.getTemporal();
        Identificador identificador = (Identificador) accesos.get(0);
        codigo += identificador.get4D(tabla, arbol);
        codigo += "=," + tabla.getTemporalActual() + ",," + temp7 + "\n";
        Object result = tabla.getVariable(identificador.getIdentificador());
        if (result instanceof String) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    (String) result,
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        Simbolo sim = (Simbolo) result;
        tipoResultado = sim.getTipo().verificarUserType(tabla);
        for (int i = 1; i < accesos.size(); i++) {
            Expresion acceso = accesos.get(i);
            if (tipoResultado.getType() == Tipo.tipo.ARREGLO) {
                if (acceso instanceof AccesoArreglo) {
                    int cantidadDimensiones = tipoResultado.getDimensiones().size(); // [2..4][5..8]
                    int cantidadAccesos = ((AccesoArreglo) acceso).getIndices().size(); // [2,7]
                    if (cantidadAccesos == cantidadDimensiones) {
                        String temp1 = tabla.getTemporal();
                        String temp2 = tabla.getTemporal();
                        String temp3 = tabla.getTemporal();
                        String temp4 = tabla.getTemporal();
                        String temp5 = tabla.getTemporal();
                        String temp6 = tabla.getTemporal();
                        //String temp8 = tabla.getTemporal();
                        String label1 = tabla.getEtiqueta();
                        String label2 = tabla.getEtiqueta();
                        codigo += "=,-1,," + temp4 + "\n";
                        for (int j = 0; j < cantidadDimensiones; j++) {
                            Dimension dimension = tipoResultado.getDimensiones().get(j);
                            codigo += dimension.getLimiteInferior().get4D(tabla, arbol);
                            codigo += "=," + tabla.getTemporalActual() + ",," + temp1 + " // Limite inferior, dimension " + j + "\n";
                            codigo += dimension.getLimiteSuperior().get4D(tabla, arbol);
                            codigo += "=," + tabla.getTemporalActual() + ",," + temp2 + " // Limite superior, dimension " + j + "\n";
                            codigo += ((AccesoArreglo) acceso).getIndices().get(j).get4D(tabla, arbol);
                            codigo += "=," + tabla.getTemporalActual() + ",," + temp3 + " // indice dimension " + j + "\n";
                            codigo += "jl," + temp3 + "," + temp1 + "," + label1 + "\n";
                            codigo += "jge," + temp3 + "," + temp2 + "," + label2 + "\n";
                            if (j > 0) {
                                codigo += "-," + temp2 + "," + temp1 + "," + temp5 + "// n" + j + "\n";
                                codigo += "*," + temp4 + "," + temp5 + "," + temp6 + "//(i-inf1)*n" + j + "\n";
                            } else {
                                codigo += "=,0,," + temp6 + "\n";
                            }
                            codigo += "-," + temp3 + "," + temp1 + "," + temp4 + "\n";
                            codigo += "+," + temp4 + "," + temp6 + "," + temp4 + "\n";
                        }
                        //codigo += "=," + temp4 + ",," + tabla.getTemporal() + "//Valor que se tiene que accesar\n";
                        codigo += "+," + temp4 + "," + temp7 + "," + temp7 + "\n";
                        if (identificador.accesoGlobal) {
                            codigo += "=,heap," + temp7 + "," + temp7 + " //Fin acceso\\n\n";
                            //codigo += "=," + temp8 + "," + tabla.getTemporalActual() + ",heap //Fin acceso\n";
                        } else {
                            codigo += "=,heap," + temp7 + "," + temp7 + " //Fin acceso\\n\n";
                            //codigo += "=," + temp8 + "," + tabla.getTemporalActual() + ",stack  //Fin acceso\n";
                        }
                        codigo += label1 + ":\n";
                        codigo += label2 + ":\n";
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
                        String temp1 = tabla.getTemporal();
                        //codigo += "=,heap," + temp7 + "," + temp1 + "\n";
                        codigo += "+," + temp7 + "," + j + "," + temp7 + "\n";
                        codigo += "=,heap," + temp7 + "," + temp7 + "\n";
                        tipoResultado = registro.getTipo();
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
        }
        codigo += "=," + temp7 + ",," + tabla.getTemporal() + "\n";
        codigo += "// fin acceso\n";
        return codigo;
    }

    public ArrayList<Expresion> getAccesos() {
        return accesos;
    }

    public void setAccesos(ArrayList<Expresion> accesos) {
        this.accesos = accesos;
    }
}
