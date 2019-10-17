/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Expresiones.Nil;
import Expresiones.Primitivo;
import Interfaces.Expresion;

/**
 *
 * @author Pavel
 */
public class Tipo {

    private tipo type;
    private String tipoObjeto;

    public static enum tipo {

        INTEGER,
        REAL,
        STRING,
        BOOLEAN,
        RECORD,
        NIL,
        WORD,
        CHAR,
        OBJETO
    }

    public Tipo(tipo type) {
        this.type = type;
    }

    public Tipo(tipo type, String tipoObjeto) {
        this.type = type;
        this.tipoObjeto = tipoObjeto;
    }

    public boolean equals(Tipo t1) {
        if (this.type == tipo.OBJETO || this.type == tipo.RECORD) {
            if (t1.getType() == tipo.NIL || this.getType() == tipo.NIL) {
                return true;
            } else {
                return this.getTipoObjeto().equalsIgnoreCase(t1.getTipoObjeto());
            }
        } else {
            return this.getType() == t1.getType();
        }
    }

    public static Expresion valorPredeterminado(Tipo t) {
        if (t.getType() == tipo.INTEGER) {
            return new Primitivo(0);
        } else if (t.getType() == tipo.REAL) {
            return new Primitivo(0.0);
        } else if (t.getType() == tipo.CHAR) {
            return new Primitivo(' ');
        } else if (t.getType() == tipo.BOOLEAN) {
            return new Primitivo(false);
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
}
