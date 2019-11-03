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
    private int fila;
    private int columna;

    public Llamada(String nombre, ArrayList<Expresion> parametros, int fila, int columna) {
        this.nombre = nombre;
        this.parametros = parametros;
        this.fila = fila;
        this.columna = columna;
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
        ArrayList<String> temporalesAux = new ArrayList<>();
        Object result = tabla.getFuncion(this.nombreFuncion);
        if (result instanceof String) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    (String) result,
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        Simbolo sim = (Simbolo) result;
        for (int i = 0; i < parametros.size(); i++) {
            codigo += parametros.get(i).get4D(tabla, arbol);
            valorParametros.add(tabla.getTemporalActual());
            tabla.AgregarTemporal(tabla.getTemporalActual());
        }
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
        for (int i = 0; i < valorParametros.size(); i++) {
            codigo += "+,p," + (i + 1) + "," + temp2 + "\n";
            codigo += "=," + temp2 + "," + valorParametros.get(i) + ",stack\n";
        }

        //codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p // Cambio de ambito\n";
        //tabla.getTempNoUsados().clear();
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
        // tabla.getIndicesGuardar().push(tamañoInicio);
        //tabla.getIndicesGuardar().pop();
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
