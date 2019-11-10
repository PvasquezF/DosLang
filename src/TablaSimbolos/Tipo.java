/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Expresiones.Nil;
import Expresiones.Primitivo;
import Interfaces.Expresion;

import java.util.ArrayList;

/**
 * @author Pavel
 */
public class Tipo {

    private tipo type;
    private tipo tipoArreglo;
    private tipo tipoRange;
    private String tipoObjeto;
    private String nombreEnum;
    private Expresion lowerLimit;
    private Expresion upperLimit;
    private ArrayList<Expresion> identificadores;
    private ArrayList<Dimension> dimensiones;
    private ArrayList<Registro> atributos;
    private boolean instancia;

    public static enum tipo {

        INTEGER,
        REAL,
        STRING,
        BOOLEAN,
        RECORD,
        NIL,
        WORD,
        CHAR,
        RANGE,
        ENUMERADO,
        ARREGLO,
        OBJETO,
        VOID
    }

    public Tipo(tipo type) {
        this.type = type;
    }

    public Tipo(tipo type, String tipoObjeto) {
        this.type = type;
        this.tipoObjeto = tipoObjeto;
    }

    public Tipo(tipo type, String tipoObjeto, Expresion lowerLimit, Expresion upperLimit) {
        this.type = type;
        this.tipoObjeto = tipoObjeto;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    public Tipo(tipo type, String tipoObjeto, ArrayList<Expresion> identificadores) {
        this.type = type;
        this.tipoObjeto = tipoObjeto;
        this.identificadores = identificadores;
    }

    public Tipo(tipo type, tipo tipoArreglo, String tipoObjeto, ArrayList<Dimension> dimensiones) {
        this.type = type;
        this.tipoArreglo = tipoArreglo;
        this.tipoObjeto = tipoObjeto;
        this.dimensiones = dimensiones;
    }

    public Tipo(ArrayList<Registro> atributos) {
        this.type = tipo.RECORD;
        this.atributos = atributos;
    }

    public Tipo(tipo type, boolean instancia) {
        this.type = type;
        this.instancia = instancia;
    }

    public boolean equals(Tipo t1) {
        if (this.type == tipo.OBJETO || this.type == tipo.RECORD) {
            if (t1.getType() == tipo.NIL || this.getType() == tipo.NIL) {
                return true;
            } else if (t1.isInstancia()) {
                return true;
            } else {
                return this.getTipoObjeto().equalsIgnoreCase(t1.getTipoObjeto());
            }
        } else if (this.getType() == tipo.ENUMERADO && t1.getType() == tipo.ENUMERADO) {
            return this.getNombreEnum().equalsIgnoreCase(t1.getNombreEnum());
        } else {
            return this.getType() == t1.getType();
        }
    }

    public static Expresion valorPredeterminado(Tipo t) {
        if (t.getType() == tipo.INTEGER || t.getType() == tipo.ENUMERADO) {
            return new Primitivo(0);
        } else if (t.getType() == tipo.REAL) {
            return new Primitivo(0.0);
        } else if (t.getType() == tipo.CHAR) {
            return new Primitivo(' ');
        } else if (t.getType() == tipo.BOOLEAN) {
            return new Primitivo(false);
        } else if (t.getType() == tipo.ARREGLO) {
            return valorPredeterminado(new Tipo(t.getTipoArreglo(), null));
        } else {
            return new Nil();
        }
    }

    @Override
    public String toString() {
        if (tipoObjeto != null) {
            return tipoObjeto;
        } else {
            return type.toString();
        }
    }

    public Tipo verificarUserType(Tabla tabla) {
        if (this.tipoObjeto != null && this.getType() != tipo.RECORD) {
            for (int i = 0; i < tabla.getListaTipos().size(); i++) {
                UserType m = tabla.getListaTipos().get(i);

                if (this.tipoObjeto.equalsIgnoreCase(m.getNombre())) {
                    // Coincide el tipoObjeto con el userType
                    if (this.getType() == tipo.ARREGLO) {
                        Tipo result = m.getTipo().verificarUserType(tabla);
                        this.setTipoArreglo(result.getType());
                        this.setTipoObjeto(result.tipoObjeto);
                        this.setAtributos(result.getAtributos());
                        return this;
                    } else {
                        Tipo result = m.getTipo().verificarUserType(tabla);
                        return result;
                    }
                }
            }
        }
        return this;
    }

    public void ReestructurarTipo(){

    }


    public tipo getType() {
        return type;
    }

    public void setType(tipo type) {
        this.type = type;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public Expresion getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Expresion lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public Expresion getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Expresion upperLimit) {
        this.upperLimit = upperLimit;
    }

    public ArrayList<Expresion> getIdentificadores() {
        return identificadores;
    }

    public void setIdentificadores(ArrayList<Expresion> identificadores) {
        this.identificadores = identificadores;
    }

    public String getNombreEnum() {
        return nombreEnum;
    }

    public void setNombreEnum(String nombreEnum) {
        this.nombreEnum = nombreEnum;
    }

    public tipo getTipoArreglo() {
        return tipoArreglo;
    }

    public void setTipoArreglo(tipo tipoArreglo) {
        this.tipoArreglo = tipoArreglo;
    }

    public ArrayList<Dimension> getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(ArrayList<Dimension> dimensiones) {
        this.dimensiones = dimensiones;
    }

    public ArrayList<Registro> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<Registro> atributos) {
        this.atributos = atributos;
    }

    public tipo getTipoRange() {
        return tipoRange;
    }

    public void setTipoRange(tipo tipoRange) {
        this.tipoRange = tipoRange;
    }

    public boolean isInstancia() {
        return instancia;
    }

    public void setInstancia(boolean instancia) {
        this.instancia = instancia;
    }
}
