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
 *
 * @author Pavel
 */
public class Tabla {

    private ArrayList<Simbolo> tabla;
    private String ambito;
    private int indiceTemporal;
    private String etiqueta;
    private int indiceEtiqueta;
    private Stack listaAmbitos;
    private int heap;
    private int stack;
    private int funcionSizeActual;
    private ArrayList<UserType> listaTipos;

    public Tabla() {
        tabla = new ArrayList<>();
        listaTipos = new ArrayList<>();
        listaAmbitos = new Stack();
        funcionSizeActual = 0;
    }

    public String InsertarVariable(Simbolo simbolo) {
        for (Simbolo i : tabla) {
            if (i.getNombre().equalsIgnoreCase(simbolo.getNombre()) && i.getAmbito().equalsIgnoreCase(simbolo.getAmbito())) {
                return "La variable " + simbolo.getNombre() + " ya ha sido declarada.";
            }
        }
        tabla.add(simbolo);
        return null;
    }

    public Object getVariable(String identificador) {
        for (Simbolo i : tabla) {
            for (Object env : listaAmbitos) {
                if (i.getNombre().equalsIgnoreCase(identificador) && i.getAmbito().equalsIgnoreCase((String) env)) {
                    return i;
                }
            }
        }
        return "No se ha encontrado la variable " + identificador + ".";
    }

    public String insertarType(UserType usertype) {
        for (UserType ut : listaTipos) {
            if (ut.getNombre().equalsIgnoreCase(usertype.getNombre())) {
                return "El type " + usertype.getNombre() + " ya existe.";
            }
        }
        this.listaTipos.add(usertype);
        return null;
    }

    public void generarTablaHTML() {
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
        encabezado += "<th scope=\"col\">Parametros</th>";
        encabezado += "<th scope=\"col\">Apuntador</th>";

        for (int i = 0; i < tabla.size(); i++) {
            Simbolo sim = tabla.get(i);
            filas += "<tr>";
            filas += "<td scope=\"row\">" + i + "</td>";
            filas += "<td scope=\"row\">" + sim.getNombre() + "</td>";
            filas += "<td scope=\"row\">" + sim.getTipo().getType() + "</td>";
            if (sim.getTipo().getType() == Tipo.tipo.ENUMERADO) {
                filas += "<td scope=\"row\">" + sim.getTipo().getNombreEnum() + "</td>";
            } else if (sim.getTipo().getType() == Tipo.tipo.OBJETO) {
                filas += "<td scope=\"row\">" + sim.getTipo().getTipoObjeto() + "</td>";
            } else {
                filas += "<td scope=\"row\"> - </td>";
            }
            filas += "<td scope=\"row\">" + sim.getAmbito() + "</td>";
            filas += "<td scope=\"row\">" + sim.isConstante() + "</td>";
            filas += "<td scope=\"row\"> - </td>";
            filas += "<td scope=\"row\">" + sim.getApuntador() + "</td>";
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
        return listaTipos;
    }

    public void setListaTipos(ArrayList<UserType> listaTipos) {
        this.listaTipos = listaTipos;
    }
}
