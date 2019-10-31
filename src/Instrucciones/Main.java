package Instrucciones;

import Excepciones.Excepcion;
import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.*;

import java.util.ArrayList;

public class Main extends Simbolo implements Instruccion {
    private ArrayList<AST> instrucciones;
    private int fila;
    private int columna;

    public Main(ArrayList<AST> instrucciones, int fila, int columna) {
        super("main", new ArrayList<>(), new Tipo(Tipo.tipo.VOID), "", "Clase principal", "Global", instrucciones, false, 0, instrucciones.size());
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        String result = tabla.InsertarFuncion(this);
        Ambito entorno = new Ambito(tabla.getEnviroment());
        String nombreAmbitoAnterior = tabla.getAmbito();
        this.setEntorno(entorno);
        this.setAmbito(nombreAmbitoAnterior);
        this.setNombreCompleto("main");
        tabla.setEnviroment(entorno);
        tabla.setAmbito(this.getNombreCompleto());
        if (result != null) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    (String) result,
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        for (int i = 0; i < instrucciones.size(); i++) {
            AST ins = instrucciones.get(i);
            Object respuesta = null;
            if (ins instanceof Instruccion) {
                respuesta = ((Instruccion) ins).ejecutar(tabla, arbol);
            } else {
                respuesta = ((Expresion) ins).getTipo(tabla, arbol);
            }
            if(respuesta instanceof Excepcion){
                return respuesta;
            }
        }
        tabla.setAmbito(nombreAmbitoAnterior);
        tabla.setEnviroment(entorno.getAnterior());
        if (tabla.getSentenciasBreakActivas().size() > 0) {
            for (Object o : tabla.getSentenciasBreakActivas()) {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        "Sentencia break fuera de ciclo.",
                        ((Break) o).getFila(), ((Break) o).getColumna());
                arbol.getErrores().add(exc);
            }
            tabla.getSentenciasBreakActivas().clear();
        }

        if (tabla.getSentenciasContinueActivas().size() > 0) {
            for (Object o : tabla.getSentenciasContinueActivas()) {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        "Sentencia continue fuera de ciclo.",
                        ((Continue) o).getFila(), ((Continue) o).getColumna());
                arbol.getErrores().add(exc);
            }
            tabla.getSentenciasContinueActivas().clear();
        }
        return null;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        tabla.getTamañoActualFuncion().push(0);
        String codigo = "// Inicio main linea: " + fila + ", columna: " + columna + "\n";
        codigo += "begin,,,main\n";
        for (AST ins : this.instrucciones) {
            codigo += ins.get4D(tabla, arbol);
        }
        for (Object o : tabla.getEtiquetasExit()) {
            codigo += (String) o + ": // Exit\n";
        }
        tabla.getEtiquetasExit().clear();
        codigo += "end,,,main\n";
        //codigo += "=," + this.instrucciones.size() + ",," + temp1 + "\n";
        //codigo += "+,p," + temp1 + ",p\n";
        codigo += "call,,,main\n";
        //codigo += "-,p," + temp1 + ",p\n";
        codigo += "//Fin main\n";
        return codigo;
    }
}
