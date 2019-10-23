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
import TablaSimbolos.Simbolo;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;
import TablaSimbolos.UserType;
import java.util.ArrayList;

/**
 *
 * @author Pavel
 */
public class DeclaracionType implements Instruccion {

    private ArrayList<String> identificadores;
    private Tipo tipo;
    private int fila;
    private int columna;

    public DeclaracionType(Tipo tipo, ArrayList<String> identificadores, int fila, int columna) {
        this.tipo = tipo;
        this.identificadores = identificadores;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        for (int i = 0; i < identificadores.size(); i++) {
            String identificador = identificadores.get(i);
            tipo.setNombreEnum(identificador);
            Object result = tabla.insertarType(new UserType(identificador, tipo));
            if (result != null) {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        (String) result,
                        fila, columna);
                arbol.getErrores().add(exc);
                return exc;
            }
            if (tipo.getType() == Tipo.tipo.ENUMERADO) {
                ArrayList<Expresion> listaId = tipo.getIdentificadores();
                for (int j = 0; j < listaId.size(); j++) {
                    Expresion m = listaId.get(j);
                    Acceso acceso = (Acceso) m;
                    Identificador id = (Identificador) acceso.getAccesos().get(0);
                    Tipo tipoEnum = new Tipo(Tipo.tipo.ENUMERADO, null);
                    tipoEnum.setNombreEnum(identificador);
                    Simbolo simboloEnum = new Simbolo(id.getIdentificador(), tipoEnum, tabla.getAmbito(), identificador + "_Enum_Item", "global", new Primitivo(j), false, tabla.getHeap());
                    Object result1 = tabla.InsertarVariable(simboloEnum);
                    if (result1 != null) {
                        Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                                (String) result1,
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
        String codigo = "";
        if (tipo.getType() == Tipo.tipo.ENUMERADO) {
            //for (int i = 0; i < tabla.getListaTipos().size(); i++) {
            for (int j = 0; j < tipo.getIdentificadores().size(); j++) {
                //Tipo t = tabla.getListaTipos().get(i).getTipo();
                Acceso acceso = (Acceso) tipo.getIdentificadores().get(j);
                Identificador identificador = (Identificador) acceso.getAccesos().get(0);
                Object result = tabla.getVariable(identificador.getIdentificador());
                if (result instanceof String) {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            (String) result,
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                } else {
                    Simbolo sim = (Simbolo) result;
                    String temp1 = tabla.getTemporal();
                    codigo += "=," + sim.getApuntador() + ",," + temp1 + "\n";
                    codigo += sim.getValor().get4D(tabla, arbol);
                    codigo += "=, " + temp1 + ", " + tabla.getTemporalActual() + ", heap\n";
                }
            }
            //}
        }
        /*for (int i = 0; i < identificadores.size(); i++) {
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
         codigo += "=," + tabla.getTemporal() + "," + sim.getApuntador() + ",heap\n";
         }
         }*/
        return codigo;
    }

    @Override
    public int getEspacios(int espacios) {
        if (tipo.getType() == Tipo.tipo.ENUMERADO) {
            espacios += tipo.getIdentificadores().size();
        }
        return espacios;
    }
}
