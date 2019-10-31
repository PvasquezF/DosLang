/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

import Excepciones.Excepcion;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pavel
 */
public class Tabla {

    private ArrayList<Simbolo> tabla;
    private String ambito;
    public int indiceTemporal;
    private String etiqueta;
    private int indiceEtiqueta;
    private Stack listaAmbitos;
    private int heap;
    private int stack;
    private int funcionSizeActual;
    private Ambito enviroment;
    private Stack tamañoActualFuncion;
    private Stack etiquetasBreak;
    private Stack sentenciasBreakActivas;
    private Stack etiquetasContinue;
    private Stack sentenciasContinueActivas;

    public Tabla() {
        this.enviroment = new Ambito(null);
        tabla = new ArrayList<>();
        listaAmbitos = new Stack();
        funcionSizeActual = 0;
        this.tamañoActualFuncion = new Stack();
        this.etiquetasBreak = new Stack();
        this.sentenciasBreakActivas = new Stack();
        this.etiquetasContinue = new Stack();
        this.sentenciasContinueActivas = new Stack();
    }

    public String InsertarVariable(Simbolo simbolo) {
        return this.enviroment.insertarVariable(simbolo);
        //for (Simbolo i : tabla) {
        //    if (i.getNombre().equalsIgnoreCase(simbolo.getNombre()) && i.getAmbito().equalsIgnoreCase(simbolo.getAmbito())) {
        //        return "La variable " + simbolo.getNombre() + " ya ha sido declarada.";
        //    }
        //}
        //tabla.add(simbolo);
        //return null;
    }

    public String InsertarFuncion(Simbolo simbolo) {
        return this.enviroment.insertarFuncion(simbolo);
        //for (Simbolo i : tabla) {
        //    if (i.getNombre().equalsIgnoreCase(simbolo.getNombre()) && i.getAmbito().equalsIgnoreCase(simbolo.getAmbito())) {
        //        return "La funcion " + simbolo.getNombre() + " ya ha sido declarada.";
        //    }
        //}
        //tabla.add(simbolo);
        //return null;
    }

    public String InsertarProcedimiento(Simbolo simbolo) {
        return this.enviroment.insertarProcedimiento(simbolo);
    }

    public Object getVariable(String identificador) {
        return this.enviroment.getVariable(identificador);
        //for (Simbolo i : tabla) {
        //    for (Object env : listaAmbitos) {
        //        if (i.getNombre().equalsIgnoreCase(identificador) && i.getAmbito().equalsIgnoreCase((String) env)) {
        //            return i;
        //        }
        //    }
        //}
        //return "No se ha encontrado la variable " + identificador + ".";
    }

    public Object getFuncion(String identificador) {
        return this.enviroment.getFuncion(identificador);
        //for (Simbolo i : tabla) {
        //    for (Object env : listaAmbitos) {
        //        if (i.getNombre().equalsIgnoreCase(identificador) && i.getAmbito().equalsIgnoreCase((String) env)) {
        //            return i;
        //        }
        //    }
        //}
        //return "No se ha encontrado la variable " + identificador + ".";
    }

    public Object getProcedimiento(String identificador) {
        return this.enviroment.getProcedimiento(identificador);
    }

    public String insertarType(UserType usertype) {
        return this.enviroment.insertarType(usertype);
        //for (UserType ut : listaTipos) {
        //    if (ut.getNombre().equalsIgnoreCase(usertype.getNombre())) {
        //        return "El type " + usertype.getNombre() + " ya existe.";
        //    }
        //}
        //this.listaTipos.add(usertype);
        //return null;
    }

    public void generarTablaHTML() {
        this.enviroment.concatenarTablas(this.tabla);
        String HTML = "<html>\n"
                + "\n"
                + "<head>\n"
                + "    <!-- Latest compiled and minified CSS -->\n"
                + "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">\n"
                + "\n"
                + "    <!-- Optional theme -->\n"
                + "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\">\n"
                + "\n"
                + "    <!-- Latest compiled and minified JavaScript -->\n"
                + "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>\n"
                + "</head>\n"
                + "\n"
                + "<body>";
        HTML += "<table class=\"table table-striped table-dark\" style=\"height: 100%;\"> ";
        String encabezado = "<tr>";
        String filas = "";
        encabezado += "<th scope=\"col\">No</th>";
        encabezado += "<th scope=\"col\">Identificador</th>";
        encabezado += "<th scope=\"col\">Tipo Primitivo</th>";
        encabezado += "<th scope=\"col\">Tipo Generado</th>";
        encabezado += "<th scope=\"col\">Ambito</th>";
        encabezado += "<th scope=\"col\">Constante</th>";
        encabezado += "<th scope=\"col\">Rol</th>";
        encabezado += "<th scope=\"col\">Parametros</th>";
        encabezado += "<th scope=\"col\">Apuntador</th>";
        encabezado += "<th scope=\"col\">Tamaño</th>";

        for (int i = 0; i < tabla.size(); i++) {
            Simbolo sim = tabla.get(i);
            filas += "<tr>";
            filas += "<td scope=\"row\">" + i + "</td>";
            filas += "<td scope=\"row\">" + sim.getNombre() + "</td>";
            filas += "<td scope=\"row\">" + sim.getTipo().getType() + "</td>";
            if (sim.getTipo().getType() == Tipo.tipo.ENUMERADO) {
                filas += "<td scope=\"row\">" + sim.getTipo().getNombreEnum() + "</td>";
            } else if (sim.getTipo().getType() == Tipo.tipo.OBJETO || sim.getTipo().getType() == Tipo.tipo.RECORD) {
                filas += "<td scope=\"row\">" + sim.getTipo().getTipoObjeto() + "</td>";
            } else if (sim.getTipo().getType() == Tipo.tipo.RANGE) {
                filas += "<td scope=\"row\">" + sim.getTipo().getTipoRange() + "</td>";
            } else if (sim.getTipo().getType() == Tipo.tipo.ARREGLO) {
                if (sim.getTipo().getTipoObjeto() != null) {
                    filas += "<td scope=\"row\">" + sim.getTipo().getTipoObjeto() + "</td>";
                } else {
                    filas += "<td scope=\"row\">" + sim.getTipo().getTipoArreglo() + "</td>";
                }
            } else {
                filas += "<td scope=\"row\"> - </td>";
            }
            filas += "<td scope=\"row\">" + sim.getAmbito() + "</td>";
            filas += "<td scope=\"row\">" + sim.isConstante() + "</td>";
            filas += "<td scope=\"row\">" + sim.getRol() + "</td>";
            filas += "<td scope=\"row\"> - </td>";
            filas += "<td scope=\"row\">" + sim.getApuntador() + "</td>";
            filas += "<td scope=\"row\">" + sim.getTamaño() + "</td>";
            filas += "</tr>";
        }
        encabezado += "</tr>";
        HTML += encabezado + filas;
        HTML += "</table>";
        HTML += "</body>\n"
                + "\n"
                + "</html>";
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("C:\\Users\\Pavel\\Desktop\\simbolos.html"), "utf-8"));
            writer.write(HTML);
        } catch (Exception ex) {
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/

            }
        }
    }

    public String getAmbito() {
        //ambito = this.listaAmbitos.firstElement().toString();
        return ambito;
    }

    public String getTemporal() {
        return "t" + indiceTemporal++;
    }

    public String getTemporalActual() {
        return "t" + (indiceTemporal - 1);
    }

    public String getEtiqueta() {
        return "L" + indiceEtiqueta++;
    }

    public String getEtiquetaActual() {
        return "L" + (indiceEtiqueta - 1);
    }

    public int getHeap() {
        return heap++;
    }

    public int getStack() {
        return stack++;
    }

    public Stack getListaAmbitos() {
        return listaAmbitos;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public int getFuncionSizeActual() {
        return funcionSizeActual;
    }

    public void setFuncionSizeActual(int funcionSizeActual) {
        this.funcionSizeActual = funcionSizeActual;
    }

    public ArrayList<UserType> getListaTipos() {
        return this.enviroment.getUserTypes();
    }

    public void setListaTipos(ArrayList<UserType> listaTipos) {
        this.enviroment.setListaTipos(listaTipos);
    }

    public Ambito getEnviroment() {
        return enviroment;
    }

    public void setEnviroment(Ambito enviroment) {
        this.enviroment = enviroment;
    }

    public Stack getTamañoActualFuncion() {
        return tamañoActualFuncion;
    }

    public void setTamañoActualFuncion(Stack tamañoActualFuncion) {
        this.tamañoActualFuncion = tamañoActualFuncion;
    }

    public Stack getEtiquetasBreak() {
        return etiquetasBreak;
    }

    public void setEtiquetasBreak(Stack etiquetasBreak) {
        this.etiquetasBreak = etiquetasBreak;
    }

    public Stack getSentenciasBreakActivas() {
        return sentenciasBreakActivas;
    }

    public void setSentenciasBreakActivas(Stack sentenciasBreakActivas) {
        this.sentenciasBreakActivas = sentenciasBreakActivas;
    }

    public Stack getEtiquetasContinue() {
        return etiquetasContinue;
    }

    public void setEtiquetasContinue(Stack etiquetasContinue) {
        this.etiquetasContinue = etiquetasContinue;
    }

    public Stack getSentenciasContinueActivas() {
        return sentenciasContinueActivas;
    }

    public void setSentenciasContinueActivas(Stack sentenciasContinueActivas) {
        this.sentenciasContinueActivas = sentenciasContinueActivas;
    }
}
