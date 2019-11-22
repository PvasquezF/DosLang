/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

/**
 *
 * @author Pavel
 */
public class UserType {

    private String nombre;
    private Tipo tipo;
    private Tipo tipoPrimitivo;

    public UserType(String nombre, Tipo tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Tipo getTipoPrimitivo() {
        return tipoPrimitivo;
    }

    public void setTipoPrimitivo(Tipo tipoPrimitivo) {
        this.tipoPrimitivo = tipoPrimitivo;
    }

}
