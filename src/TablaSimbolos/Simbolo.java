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
public class Simbolo {

    private String nombre;
    private Tipo tipo;
    private String ambito;
    private String rol;
    private int apuntador;

    public Simbolo(String nombre, Tipo tipo, String ambito, String rol, int apuntador) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.rol = rol;
        this.apuntador = apuntador;
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

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getApuntador() {
        return apuntador;
    }

    public void setApuntador(int apuntador) {
        this.apuntador = apuntador;
    }
}
