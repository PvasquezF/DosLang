/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Interfaces.Expresion;

/**
 *
 * @author Pavel
 */
public class Simbolo {

    private String nombre;
    private Tipo tipo;
    private String ambito;
    private String rol;
    private String nivel;
    private int apuntador;
    private boolean constante;
    private Expresion valor;
    private boolean primitivo;

    public Simbolo(String nombre, Tipo tipo, String ambito, String rol, String nivel, boolean constante, int apuntador) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.rol = rol;
        this.nivel = nivel;
        this.apuntador = apuntador;
        this.constante = constante;
    }
    
    public Simbolo(String nombre, Tipo tipo, String ambito, String rol, String nivel, Expresion valor, boolean constante, int apuntador) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.rol = rol;
        this.nivel = nivel;
        this.apuntador = apuntador;
        this.valor = valor;
        this.constante = constante;
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

    public boolean isConstante() {
        return constante;
    }

    public void setConstante(boolean constante) {
        this.constante = constante;
    }

    public Expresion getValor() {
        return valor;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public boolean isPrimitivo() {
        if(tipo.getType() != Tipo.tipo.OBJETO || tipo.getType() != Tipo.tipo.RECORD){
            primitivo = false;
        }else{
            primitivo = true;
        }
        return primitivo;
    }

    public void setPrimitivo(boolean primitivo) {
        this.primitivo = primitivo;
    }
}
