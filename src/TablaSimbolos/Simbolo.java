/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Interfaces.AST;
import Interfaces.Expresion;

import java.util.ArrayList;

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
    private int apuntadorRef;
    private int tamaño;
    private boolean constante;
    private Expresion valor;
    private boolean primitivo;
    private ArrayList<AST> instrucciones;
    private ArrayList<AST> parametros;

    public Simbolo(String nombre, Tipo tipo, String ambito, String rol, String nivel, boolean constante, int apuntador) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.rol = rol;
        this.nivel = nivel;
        this.apuntador = apuntador;
        this.constante = constante;
        this.apuntadorRef = -1;
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
        this.apuntadorRef = -1;
    }

    public Simbolo(String nombre, Tipo tipo, String ambito, String rol, String nivel, boolean constante, int apuntador, int apuntadorRef) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.rol = rol;
        this.nivel = nivel;
        this.apuntador = apuntador;
        this.constante = constante;
        this.apuntadorRef = apuntadorRef;
    }

    public Simbolo(String nombre, Tipo tipo, String ambito, String rol, String nivel, Expresion valor, boolean constante, int apuntador, int apuntadorRef) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.rol = rol;
        this.nivel = nivel;
        this.apuntador = apuntador;
        this.valor = valor;
        this.constante = constante;
        this.apuntadorRef = apuntadorRef;
    }

    public Simbolo(String nombre, ArrayList<AST> parametros, Tipo tipo, String ambito, String rol, String nivel, ArrayList<AST> instrucciones, boolean constante, int apuntador, int tamaño) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.rol = rol;
        this.nivel = nivel;
        this.apuntador = apuntador;
        this.instrucciones = instrucciones;
        this.constante = constante;
        this.tamaño = tamaño;
        this.parametros = parametros;
        this.apuntadorRef = -1;
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

    public ArrayList<AST> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<AST> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public int getTamaño() {
        return tamaño;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    public int getApuntadorRef() {
        return apuntadorRef;
    }

    public void setApuntadorRef(int apuntadorRef) {
        this.apuntadorRef = apuntadorRef;
    }
}
