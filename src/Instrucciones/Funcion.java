package Instrucciones;

import Excepciones.Excepcion;
import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.*;

import java.util.ArrayList;

public class Funcion extends Simbolo implements Instruccion {
    // String nombre, ArrayList<AST> parametros, Tipo tipo, String ambito,
    // String rol, String nivel, ArrayList<AST> instrucciones, boolean constante, int apuntador, int tamaño
    private ArrayList<AST> instrucciones;
    private ArrayList<AST> variables;
    private int fila;
    private int columna;
    private int TemporalInicio;
    private int TemporalFin;

    public Funcion(String nombre, ArrayList<Parametro> parametros, Tipo tipo, ArrayList<AST> instrucciones, ArrayList<AST> variables, int fila, int columna) {
        super(nombre, nombre, parametros, tipo, 0, null);
        this.instrucciones = instrucciones;
        this.variables = variables;
        this.fila = fila;
        this.columna = columna;
        this.setNombreCompleto(generarNombreCompleto());
        this.setRol("funcion");
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
        int tamaño = 0;
        Simbolo simbolo = null;
        String nombre = this.getNombre();
        Ambito entorno = new Ambito(tabla.getEnviroment());
        Tipo tipoAux = this.getTipo().verificarUserType(tabla);
        String nombreAmbitoAnterior = tabla.getAmbito();

        this.setNombreCompleto(generarNombreCompleto());
        this.setAmbito(generarNombreCompleto());
        this.setEntorno(entorno);
        this.setAmbito(nombreAmbitoAnterior);
        tabla.setEnviroment(entorno);
        tabla.setAmbito(this.getNombreCompleto());
        simbolo = new Simbolo(nombre, tipoAux, tabla.getAmbito(), "retorno", "local", Tipo.valorPredeterminado(tipoAux), false, tabla.getEnviroment().getPosicionStack(), false);
        tabla.InsertarVariable(simbolo);
        for (int i = 0; i < instrucciones.size(); i++) {
            AST ins = instrucciones.get(i);
            if (ins instanceof Funcion) {
                Funcion f = (Funcion) ins;
                tabla.InsertarFuncion(f);
                for (int j = 0; j < f.getParametros().size(); j++) {
                    Parametro parametro = f.getParametros().get(j);
                    parametro.setTipo(parametro.getTipo().verificarUserType(tabla));
                }
                f.setNombreCompleto(f.generarNombreCompleto());
            } else if (ins instanceof Procedimiento) {
                Procedimiento p = (Procedimiento) ins;
                tabla.InsertarFuncion(p);
                for (int j = 0; j < p.getParametros().size(); j++) {
                    Parametro parametro = p.getParametros().get(j);
                    parametro.setTipo(parametro.getTipo().verificarUserType(tabla));
                }
                p.setNombreCompleto(p.generarNombreCompleto());
            }
        }
        for (int i = 0; i < this.getParametros().size(); i++) {
            Parametro parametro = this.getParametros().get(i);
            parametro.ejecutar(tabla, arbol);
            tamaño += parametro.getIdentificador().size();
        }

        for (int i = 0; i < this.variables.size(); i++) {
            DeclaracionVar declaracion = (DeclaracionVar) this.variables.get(i);
            declaracion.setStack(true);
            tamaño += declaracion.getEspacios(tamaño);
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
            if (respuesta instanceof Excepcion) {
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
        //tabla.getTempNoUsados().clear();
        tabla.getTamañoActualFuncion().push(this.getTamaño());
        tabla.getTempNoUsados().clear();
        String codigo = "";
        int indiceInicio = 0;
        int indiceFinal = 0;
        indiceInicio = tabla.indiceTemporal;
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
            if (instruccion instanceof Funcion || instruccion instanceof Procedimiento) {
                continue;
            } else {
                codigo += instruccion.get4D(tabla, arbol);
            }
        }
        for (Object o : tabla.getEtiquetasExit()) {
            codigo += (String) o + ": // Exit\n";
        }
        tabla.getEtiquetasExit().clear();
        codigo += "end,,," + this.getNombreCompleto() + "\n";
        tabla.getTamañoActualFuncion().pop();
        for (int i = 0; i < this.instrucciones.size(); i++) {
            AST instruccion = instrucciones.get(i);
            if (instruccion instanceof Funcion || instruccion instanceof Procedimiento) {
                codigo += instruccion.get4D(tabla, arbol);
            } else {
                continue;
            }
        }
        tabla.setEnviroment(this.getEntorno().getAnterior());
        return codigo;
    }
}
