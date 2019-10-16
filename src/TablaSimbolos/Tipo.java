/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

/**
 *
 * @author Pavel
 */
public class Tipo {

    private tipo t;
    private String tipoObjeto;

    public static enum tipo {

        INTEGER,
        REAL,
        STRING,
        BOOLEAN,
        RECORD,
        NIL,
        WORD,
        CHAR
    }

    public Tipo(tipo t) {
        this.t = t;
    }

    public Tipo(tipo t, String tipoObjeto) {
        this.t = t;
        this.tipoObjeto = tipoObjeto;
    }

    public tipo getT() {
        return t;
    }

    public void setT(tipo t) {
        this.t = t;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }
}
