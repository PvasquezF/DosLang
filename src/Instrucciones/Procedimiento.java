package Instrucciones;

import Excepciones.Excepcion;
import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.*;

import java.util.ArrayList;

public class Procedimiento extends Simbolo implements Instruccion {
    private ArrayList<AST> instrucciones;
    private ArrayList<AST> variables;
    private int fila;
    private int columna;

    public Procedimiento(String nombre, ArrayList<Parametro> parametros, Tipo tipo, ArrayList<AST> instrucciones, ArrayList<AST> variables, int fila, int columna) {
        super(nombre, nombre, parametros, tipo, 0, null);
        this.instrucciones = instrucciones;
        this.variables = variables;
        this.fila = fila;
        this.columna = columna;
        this.setNombreCompleto(generarNombreCompleto());
        this.setRol("procedimiento");
    }

    public String generarNombreCompleto() {
        String nombreCompleto = this.getNombre();
        for (int i = 0; i < this.getParametros().size(); i++) {
            Tipo t = this.getParametros().get(i).getTipo();
            ArrayList<String> lista = this.getParametros().get(i).getIdentificador();
            for (String item : lista) {
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
        }
        return nombreCompleto;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        Ambito entorno = new Ambito(tabla.getEnviroment());
        String nombreAmbitoAnterior = tabla.getAmbito();
        int tamaño = 0;
        this.setEntorno(entorno);
        this.setAmbito(nombreAmbitoAnterior);
        tabla.setEnviroment(entorno);
        Simbolo simbolo = null;
        String nombre = this.getNombre();
        Tipo tipoAux = this.getTipo().verificarUserType(tabla);
        tabla.setAmbito(this.getNombreCompleto());

        simbolo = new Simbolo(nombre, tipoAux, tabla.getAmbito(), "retorno", "local", Tipo.valorPredeterminado(tipoAux), false,tabla.getEnviroment().getPosicionStack(), false);
        tabla.InsertarVariable(simbolo);
        for (int i = 0; i < this.getParametros().size(); i++) {
            Parametro parametro = this.getParametros().get(i);
            parametro.setTipo(parametro.getTipo().verificarUserType(tabla));
            parametro.ejecutar(tabla, arbol);
            tamaño += parametro.getIdentificador().size();
            tamaño += 1;
        }
        for (int i = 0; i < this.variables.size(); i++) {
            DeclaracionVar declaracion = (DeclaracionVar) this.variables.get(i);
            declaracion.setStack(true);
            tamaño = declaracion.getIdentificadores().size();
            declaracion.ejecutar(tabla, arbol);
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

        tamaño += 1;
        this.setTamaño(tamaño);
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
        tabla.getTamañoActualFuncion().push(this.getTamaño());
        String codigo = "";
        tabla.setEnviroment(this.getEntorno());
        String temp1 = tabla.getTemporal();
        codigo += "begin,,," + this.getNombreCompleto() + "\n";
        codigo += "+,p,0," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        codigo += "=," + temp1 + ",-1,stack\n";
        tabla.QuitarTemporal(temp1);
        for (int i = 0; i < this.variables.size(); i++) {
            DeclaracionVar declaracion = (DeclaracionVar) this.variables.get(i);
            declaracion.setStack(true);
            codigo += declaracion.get4D(tabla, arbol);
        }
        for (int i = 0; i < this.instrucciones.size(); i++) {
            AST instruccion = instrucciones.get(i);
            codigo += instruccion.get4D(tabla, arbol);
        }
        for (Object o : tabla.getEtiquetasExit()) {
            codigo += (String) o + ": // Exit\n";
        }
        tabla.getEtiquetasExit().clear();
        codigo += "end,,," + this.getNombreCompleto() + "\n";
        tabla.setEnviroment(this.getEntorno().getAnterior());
        tabla.getTamañoActualFuncion().pop();
        return codigo;
    }
}
