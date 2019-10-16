/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Excepciones.Excepcion;
import java.util.ArrayList;

/**
 *
 * @author Pavel
 */
public class Tabla {
    private ArrayList<Simbolo> tabla;
    private String ambito;
    private String temporal;
    private String etiqueta;
    private int heap;
    private int stack;

    public Tabla(){
        tabla = new ArrayList<>();
    }
    
    public String InsertarVariable(Simbolo simbolo){
        for(Simbolo i : tabla){
            if(i.getNombre().equals(simbolo.getNombre())){
                return "La variable " + simbolo.getNombre() + " ya ha sido declarada.";
            }
        }
        return null;
    }
    
    public ArrayList<Simbolo> getTabla() {
        return tabla;
    }

    public void setTabla(ArrayList<Simbolo> tabla) {
        this.tabla = tabla;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getTemporal() {
        return temporal;
    }

    public void setTemporal(String temporal) {
        this.temporal = temporal;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }   

    public int getHeap() {
        return heap++;
    }

    public int getStack() {
        return stack;
    }
}
