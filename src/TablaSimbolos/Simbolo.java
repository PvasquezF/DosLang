/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Expresiones.Acceso;
import Instrucciones.AccesoVariable;
import Interfaces.AST;
import Interfaces.Expresion;

import java.util.ArrayList;

/**
 *
 * @author Pavel
 */
public class Simbolo {

    private String nombre;
    private String nombreCompleto;
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
    private boolean referencia;
    private Ambito entorno;
    private ArrayList<AST> instrucciones;
    private ArrayList<Parametro> parametros;
    private boolean isWith;
    private AccesoVariable accesoVariable;
    private Acceso acceso;

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

    public Simbolo(String nombre, Tipo tipo, String ambito, String rol, String nivel, Expresion valor, boolean constante, int apuntador, boolean referencia) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.rol = rol;
        this.nivel = nivel;
        this.valor = valor;
        this.apuntador = apuntador;
        this.constante = constante;
        this.referencia = referencia;
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

    public Simbolo(String nombre, ArrayList<Parametro> parametros, Tipo tipo, String ambito, String rol, String nivel, ArrayList<AST> instrucciones, boolean constante, int apuntador, int tamaño) {
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

    public Simbolo(String nombre, String nombreCompleto, ArrayList<Parametro> parametros, Tipo tipo, int tamaño, Ambito entorno) {
        this.nombre = nombre;
        this.nombreCompleto = nombreCompleto;
        this.tipo = tipo;
        this.tamaño = tamaño;
        this.parametros = parametros;
        this.entorno = entorno;
        this.apuntadorRef = -1;
    }

    public Simbolo(String nombre, Tipo tipo, String ambito, String rol, String nivel, Expresion valor, boolean constante, int apuntador, boolean isWith, AccesoVariable accesoVariable) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.rol = rol;
        this.nivel = nivel;
        this.valor = valor;
        this.apuntador = apuntador;
        this.constante = constante;
        this.isWith = isWith;
        this.accesoVariable = accesoVariable;
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

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public ArrayList<Parametro> getParametros() {
        return parametros;
    }

    public void setParametros(ArrayList<Parametro> parametros) {
        this.parametros = parametros;
    }

    public void setValor(Expresion valor) {
        this.valor = valor;
    }

    public Ambito getEntorno() {
        return entorno;
    }

    public void setEntorno(Ambito entorno) {
        this.entorno = entorno;
    }

    public boolean isReferencia() {
        return referencia;
    }

    public void setReferencia(boolean referencia) {
        this.referencia = referencia;
    }

    public boolean isWith() {
        return isWith;
    }

    public void setWith(boolean with) {
        isWith = with;
    }

    public AccesoVariable getAccesoVariable() {
        return accesoVariable;
    }

    public void setAccesoVariable(AccesoVariable accesoVariable) {
        this.accesoVariable = accesoVariable;
    }

    public Acceso getAcceso() {
        return acceso;
    }

    public void setAcceso(Acceso acceso) {
        this.acceso = acceso;
    }
}
