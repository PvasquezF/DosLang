/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import com.sun.org.apache.xpath.internal.operations.Equals;

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

    public Tipo(tipo t, String tipoObjeto) {
        this.type = type;
        this.tipoObjeto = tipoObjeto;
    }

    public boolean equals(Tipo t1) {
        if (this.type == tipo.OBJETO || this.type == tipo.RECORD) {
            return this.getTipoObjeto().equalsIgnoreCase(t1.getTipoObjeto());
        } else {
            return this.getType() == t1.getType();
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
