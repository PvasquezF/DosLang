package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.*;

import java.util.ArrayList;
import java.util.Collections;

public class Llamada implements Expresion {
    private String nombre;
    private String nombreFuncion;
    private ArrayList<Expresion> parametros;
    private ArrayList<Parametro> parametrosFuncionLlamar;
    private ArrayList<Boolean> parametroLocalGlobal;
    private ArrayList<Boolean> parametroLlamadaisReferencia;
    private ArrayList<Boolean> parametrosRefValida; // para saber si lo pasa o no por ref
    private int fila;
    private int columna;

    public Llamada(String nombre, ArrayList<Expresion> parametros, int fila, int columna) {
        this.nombre = nombre;
        this.parametros = parametros;
        this.fila = fila;
        this.columna = columna;
        parametrosFuncionLlamar = new ArrayList<>();
        parametroLocalGlobal = new ArrayList<>();
        parametroLlamadaisReferencia = new ArrayList<>();
        parametrosRefValida = new ArrayList<>();
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        String nombreCompleto = "";
        Object respuestaNombre = generarNombreCompleto(tabla, arbol);
        if (respuestaNombre instanceof Excepcion) {
            return respuestaNombre;
        }
        nombreCompleto = (String) respuestaNombre;
        this.nombreFuncion = nombreCompleto;
        Object result = tabla.getFuncion(nombreCompleto);
        if (result instanceof String) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    (String) result,
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        Simbolo sim = (Simbolo) result;

        for (int i = 0; i < this.parametros.size(); i++) {
            Object res = this.parametros.get(i).getTipo(tabla, arbol);
            if (res instanceof Excepcion) {
                return res;
            }
            if (this.parametros.get(i) instanceof Acceso) {
                Acceso a = (Acceso) this.parametros.get(i);
                Identificador iden = (Identificador) a.getAccesos().get(0);
                parametroLocalGlobal.add(a.accesoGlobal);

                Object s = tabla.getVariable(iden.getIdentificador());
                if (s instanceof String) {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            sim.toString(),
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
                Simbolo si = (Simbolo) s;
                if (si.getTipo().getType() == Tipo.tipo.INTEGER
                        || si.getTipo().getType() == Tipo.tipo.REAL
                        || si.getTipo().getType() == Tipo.tipo.BOOLEAN
                        || si.getTipo().getType() == Tipo.tipo.CHAR
                        || si.getTipo().getType() == Tipo.tipo.WORD
                        || si.getTipo().getType() == Tipo.tipo.STRING) {
                    parametrosRefValida.add(true);
                } else {
                    parametrosRefValida.add(false);
                }
                parametroLlamadaisReferencia.add(si.isReferencia());
            } else {
                parametrosRefValida.add(false);
                parametroLocalGlobal.add(false);
            }
        }

        for (int i = 0; i < sim.getParametros().size(); i++) {
            Parametro p = sim.getParametros().get(i);
            for (int j = 0; j < p.getIdentificador().size(); j++) {
                ArrayList<String> id = new ArrayList<>();
                id.add(p.getIdentificador().get(j));
                parametrosFuncionLlamar.add(new Parametro(p.getTipo(), id, p.isReferencia(), p.getFila(), p.getColumna()));
            }
        }
        return sim.getTipo();
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        return null;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Llamada\n";
        ArrayList<String> valorParametros = new ArrayList<>();
        ArrayList<String> temporalesRecuperar = new ArrayList<>();
        Object result = tabla.getFuncion(this.nombreFuncion);

        Simbolo sim = (Simbolo) result;
        for (int i = 0; i < parametros.size(); i++) {
            if (parametros.get(i) instanceof Acceso
                    && parametrosFuncionLlamar.get(i).isReferencia()
                    && parametrosRefValida.get(i)) {
                Acceso a = (Acceso) parametros.get(i);
                ((Identificador) a.getAccesos().get(0)).asExpresion = false;
            }
            codigo += parametros.get(i).get4D(tabla, arbol);
            if (parametros.get(i) instanceof Acceso
                    && parametrosFuncionLlamar.get(i).isReferencia()
                    && parametrosRefValida.get(i)) {
                Acceso a = (Acceso) parametros.get(i);
                ((Identificador) a.getAccesos().get(0)).asExpresion = true;
            }

            valorParametros.add(tabla.getTemporalActual());
            tabla.AgregarTemporal(tabla.getTemporalActual());
        }
        String t4 = tabla.getTemporal();
        codigo += "=,p,," + t4 + "\n";
        codigo += "// Cambio de ambito\n";
        codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "// FIN Cambio de ambito\n";

        temporalesRecuperar.addAll(tabla.getTempNoUsados());
        String temp3 = tabla.getTemporal();
        codigo += "// Guardando temp\n";
        for (int i = 0; i < tabla.getTempNoUsados().size(); i++) {
            codigo += "+,p," + i + "," + temp3 + "\n";
            codigo += "=," + temp3 + "," + tabla.getTempNoUsados().get(i) + ",stack\n";
        }
        codigo += "+,p," + tabla.getTempNoUsados().size() + ",p\n";
        codigo += "// Fin Guardando temp\n";

        String temp2 = tabla.getTemporal();
        int k = 1;
        for (int i = 0; i < valorParametros.size(); i++) {
            if (parametrosFuncionLlamar.get(i).isReferencia() && parametrosRefValida.get(i)) {
                if (parametroLlamadaisReferencia.get(i)) {
                    String t2 = tabla.getTemporal();
                    String t3 = tabla.getTemporal();
                    String t5 = tabla.getTemporal();
                    tabla.AgregarTemporal(t2);
                    tabla.AgregarTemporal(t3);
                    tabla.AgregarTemporal(t5);
                    codigo += "+," + t4 + "," + valorParametros.get(i) + "," + t2 + "\n";
                    tabla.QuitarTemporal(t4);
                    tabla.QuitarTemporal(valorParametros.get(i));
                    tabla.AgregarTemporal(t2);
                    codigo += "=,stack," + t2 + "," + t3 + "\n";
                    tabla.QuitarTemporal(t2);
                    tabla.AgregarTemporal(t3);

                    codigo += "+,p," + k++ + "," + temp2 + "\n";
                    tabla.AgregarTemporal(temp2);
                    codigo += "=," + temp2 + "," + t3 + ",stack\n";
                    tabla.QuitarTemporal(temp2);
                    tabla.QuitarTemporal(t3);
                    codigo += "+," + t2 + "," + 1 + "," + t2 + "\n";
                    tabla.AgregarTemporal(t2);
                    codigo += "=,stack," + t2 + "," + t5 + "\n";
                    tabla.QuitarTemporal(t2);
                    tabla.AgregarTemporal(t5);
                    codigo += "+,p," + k++ + "," + temp2 + "\n";
                    tabla.AgregarTemporal(temp2);
                    codigo += "=," + temp2 + "," + t5 + ",stack\n";
                    tabla.QuitarTemporal(temp2);
                    tabla.QuitarTemporal(t5);

                } else {
                    if (parametroLocalGlobal.get(i)) {
                        codigo += "+,p," + k++ + "," + temp2 + "\n";
                        tabla.AgregarTemporal(temp2);
                        codigo += "=," + temp2 + "," + valorParametros.get(i) + ",stack\n";
                        tabla.QuitarTemporal(valorParametros.get(i));
                        tabla.QuitarTemporal(temp2);
                        codigo += "+,p," + k++ + "," + temp2 + "\n";
                        tabla.AgregarTemporal(temp2);
                        codigo += "=," + temp2 + "," + (parametroLocalGlobal.get(i) ? 1 : 0) + ",stack\n";
                        tabla.QuitarTemporal(temp2);
                    } else {
                        String t2 = tabla.getTemporal();
                        tabla.AgregarTemporal(t2);
                        codigo += "+," + t4 + "," + valorParametros.get(i) + "," + t2 + "\n";
                        tabla.QuitarTemporal(t4);
                        tabla.QuitarTemporal(valorParametros.get(i));
                        tabla.AgregarTemporal(t2);
                        codigo += "+,p," + k++ + "," + temp2 + "\n";
                        tabla.AgregarTemporal(temp2);
                        codigo += "=," + temp2 + "," + t2 + ",stack\n";
                        tabla.QuitarTemporal(temp2);
                        tabla.QuitarTemporal(t2);
                        codigo += "+,p," + k++ + "," + temp2 + "\n";
                        tabla.AgregarTemporal(temp2);
                        codigo += "=," + temp2 + "," + (parametroLocalGlobal.get(i) ? 1 : 0) + ",stack\n";
                        tabla.QuitarTemporal(temp2);
                    }
                }
            } else {
                codigo += "+,p," + k++ + "," + temp2 + "\n";
                tabla.AgregarTemporal(temp2);
                codigo += "=," + temp2 + "," + valorParametros.get(i) + ",stack\n";
                tabla.QuitarTemporal(temp2);
                tabla.QuitarTemporal(valorParametros.get(i));
                codigo += "+,p," + k++ + "," + temp2 + "\n";
                tabla.AgregarTemporal(temp2);
                codigo += "=," + temp2 + "," + (parametroLocalGlobal.get(i) ? 1 : 0) + ",stack\n";
                tabla.QuitarTemporal(temp2);
            }
        }

        codigo += "call,,," + sim.getNombreCompleto() + "\n";
        String temp4 = tabla.getTemporal();
        codigo += "+,p,0," + temp4 + "// Retorno\n";
        codigo += "-,p," + temporalesRecuperar.size() + ",p\n";
        codigo += "// Recuperando temp\n";
        String temp5 = tabla.getTemporal();
        for (int i = 0; i < temporalesRecuperar.size(); i++) {
            codigo += "+,p," + i + "," + temp5 + "\n";
            codigo += "=,stack," + temp5 + "," + temporalesRecuperar.get(i) + "\n";
        }
        codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p // Cambio de ambito\n";
        codigo += "// Fin Recuperando temp\n";
        codigo += "=,stack," + temp4 + "," + tabla.getTemporal() + "\n";

        tabla.getTempNoUsados().clear();
        codigo += "// Fin Llamada\n";
        return codigo;
    }

    public Object generarNombreCompleto(Tabla tabla, Tree arbol) {
        String nombreCompleto = this.nombre;
        for (int i = 0; i < this.parametros.size(); i++) {
            Expresion exp = this.parametros.get(i);
            Object result = exp.getTipo(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Tipo t = (Tipo) result;
            if (t.getType() == Tipo.tipo.ARREGLO) {
                nombreCompleto += "_" + t.getType() + "_" + t.getTipoArreglo();
                if (t.getTipoArreglo() == Tipo.tipo.RECORD) {
                    nombreCompleto += "_" + t.getTipoObjeto();
                }
            } else if (t.getType() == Tipo.tipo.RECORD) {
                nombreCompleto += "_" + t.getType() + "_" + t.getTipoObjeto();
            } else {
                nombreCompleto += "_" + t.getType();
            }
        }
        return nombreCompleto;
    }
}
