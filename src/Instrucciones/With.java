package Instrucciones;

import Excepciones.Excepcion;
import Expresiones.Acceso;
import Expresiones.Identificador;
import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.*;

import java.util.ArrayList;

public class With implements Instruccion {
    private AccesoVariable variable;
    private ArrayList<AST> instrucciones;
    private Ambito entorno;
    private int fila;
    private int columna;

    public With(AccesoVariable variable, ArrayList<AST> instrucciones, int fila, int columna) {
        this.variable = variable;
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        Object result = variable.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        Tipo tipo = (Tipo) result;
        Ambito entorno = new Ambito(tabla.getEnviroment());
        this.entorno = entorno;
        tabla.setEnviroment(entorno);
        if (tipo.getType() == Tipo.tipo.RECORD) {
            ArrayList<Registro> atributos = tipo.getAtributos();
            String ambito = ((Identificador) variable.getAccesos().get(0)).getIdentificador();
            for (int i = 0; i < atributos.size(); i++) {
                Registro r = atributos.get(i);
                Simbolo simbolo = new Simbolo(r.getIdentificador(), r.getTipo(), ambito, "Atributo", "global", Tipo.valorPredeterminado(r.getTipo()), false, i, true, variable);
                tabla.InsertarVariable(simbolo);
            }
        } else {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    "Se esperaba un registro y encontro un tipo " + tipo.getType() + ", para la sentencia with.",
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
            if (respuesta instanceof Excepcion) {
                return respuesta;
            }
        }
        tabla.setEnviroment(entorno.getAnterior());
        return null;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        tabla.setEnviroment(entorno);
        Object result = variable.getTipo(tabla, arbol);
        String ambito = ((Identificador) variable.getAccesos().get(0)).getIdentificador();
        Simbolo sim = (Simbolo) tabla.getVariable(ambito);
        Tipo tipo = (Tipo) result;
        Ambito entorno = new Ambito(tabla.getEnviroment());
        tabla.setEnviroment(entorno);
        codigo += variable.get4D(tabla, arbol);
        for (int i = 0; i < this.instrucciones.size(); i++) {
            AST instruccion = instrucciones.get(i);
            codigo += instruccion.get4D(tabla, arbol);
        }
        tabla.setEnviroment(entorno.getAnterior());
        return codigo;
    }
}
