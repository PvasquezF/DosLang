package Conexion;

import Excepciones.Excepcion;
import Instrucciones.*;
import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import LexicoDosLang.Lexer;
import SintacticoDosLang.Syntax;
import TablaSimbolos.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class Compilacion {
    public static ArrayList<Excepcion> errores;

    public Compilacion() {

    }

    public String compilar(String entrada) {
        errores = new ArrayList<>();
        Lexer lexer = new Lexer(new BufferedReader(new StringReader(entrada)));
        Syntax s = new Syntax(lexer);
        try {
            s.parse();
            Tree t = s.getArbol();
            Tabla tabla = new Tabla();
            String Cuadruplos = "";
            int espaciosReservaHeap = 0;
            for (int i = 0; i < t.getInstrucciones().size(); i++) {
                Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
                if (ins instanceof Program) {
                    ins.ejecutar(tabla, t);
                }
            }
            for (int i = 0; i < t.getInstrucciones().size(); i++) {
                Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
                if (ins instanceof DeclaracionType) {
                    ins.ejecutar(tabla, t);
                }
            }

            AgregarFunciones(tabla, t.getInstrucciones());
            for (int i = 0; i < t.getInstrucciones().size(); i++) {
                AST ins = t.getInstrucciones().get(i);
                Object respuesta = null;
                if (ins instanceof Instruccion) {
                    if (!(ins instanceof DeclaracionType)) {
                        ((Instruccion) ins).ejecutar(tabla, t);
                    }
                } else {
                    ((Expresion) ins).getTipo(tabla, t);
                }
            }
            for (int i = 0; i < t.getInstrucciones().size(); i++) {
                Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
                if (ins instanceof DeclaracionConstante
                        || ins instanceof DeclaracionVar
                        || ins instanceof DeclaracionType
                        || ins instanceof Uses) {
                    espaciosReservaHeap = ins.getEspacios(espaciosReservaHeap);
                }
            }

            Cuadruplos += ReservarMemoria.Reservar(tabla, espaciosReservaHeap);
            errores.addAll(t.getErrores());
            String jsonEnvio = "{\n";
            if (errores.size() == 0) {
                for (int i = 0; i < t.getInstrucciones().size(); i++) {
                    Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
                    Cuadruplos += ins.get4D(tabla, t);
                }

                GenerarNativas4D gn4D = new GenerarNativas4D();
                Cuadruplos += gn4D.generarConcatenacion(tabla);
                Cuadruplos += gn4D.generarPotencia(tabla);
                Cuadruplos += gn4D.generarNullPointerException(tabla);
                Cuadruplos += gn4D.generarRangoFueraLimites(tabla);
                Cuadruplos += gn4D.generarIndexOut(tabla);
                Cuadruplos += gn4D.generarIntToString(tabla);

                Cuadruplos += "\n\n\n\n// ----------------------------------INICIO Funciones Que se pueden quitar-------------------------\n\n\n\n";
                Cuadruplos += gn4D.generarConcatenacionCharString(tabla);
                Cuadruplos += gn4D.generarConcatenacionStringChar(tabla);
                Cuadruplos += gn4D.generarStringToBoolean(tabla);
                Cuadruplos += "\n\n\n\n// ----------------------------------FIN Funciones Que se pueden quitar-------------------------\n\n\n\n";
                // Funciones primitivas
                Cuadruplos += "\n\n\n\n// ----------------------------------INICIO NATIVAS-------------------------\n\n\n\n";
                Cuadruplos += gn4D.generarChartAt(tabla);
                Cuadruplos += gn4D.generarLenght(tabla);
                Cuadruplos += gn4D.generaReplace(tabla);
                Cuadruplos += gn4D.generarToCharArray(tabla);
                Cuadruplos += gn4D.generarLowerCase(tabla);
                Cuadruplos += gn4D.generarUpperCase(tabla);
                Cuadruplos += gn4D.generarEquals(tabla);
                Cuadruplos += "\n\n\n\n// ----------------------------------FIN NATIVAS-------------------------\n\n\n\n";

                Cuadruplos += "\n\n\n\n// ----------------------------------INICIO NO VAN EN ASSEMBLER PORQUE NO JALAN-------------------------\n\n\n\n";
                Cuadruplos += gn4D.generarRealToString(tabla);
                Cuadruplos += gn4D.generarTrunk(tabla);
                Cuadruplos += gn4D.generarRound(tabla);
                Cuadruplos += "\n\n\n\n// ----------------------------------FIN NO VAN EN ASSEMBLER PORQUE NO JALAN-------------------------\n\n\n\n";
                //Cuadruplos += gn4D.generarPrint(tabla);
                //System.out.println(Cuadruplos);
                Cuadruplos = Cuadruplos.replaceAll("\n", "\\\\n");
                jsonEnvio += "\"Cuadruplos\": \"" + Cuadruplos + "\",\n";
            } else {
                jsonEnvio += "\"Cuadruplos\": \"\",\n";
            }
            String htmlErrores = toHtmlErrors();
            htmlErrores = htmlErrores.replaceAll("\n", "\\\\n");
            htmlErrores = htmlErrores.replaceAll("\\\"", "\\\\\"");
            jsonEnvio += "\"Errores\":\"" + htmlErrores + "\",\n";
            String htmlTabla = tabla.generarTablaHTML();
            htmlTabla = htmlTabla.replaceAll("\n", "\\\\n");
            htmlTabla = htmlTabla.replaceAll("\\\"", "\\\\\"");
            jsonEnvio += "\"Simbolos\":\"" + htmlTabla + "\"\n";
            jsonEnvio += "}\n";

            return jsonEnvio;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String jsonEnvio = "{\n";
            jsonEnvio += "\"Cuadruplos\": \"\",\n";
            String htmlErrores = toHtmlErrors();
            htmlErrores = htmlErrores.replaceAll("\n", "\\\\n");
            htmlErrores = htmlErrores.replaceAll("\\\"", "\\\\\"");
            jsonEnvio += "\"Errores\":\"" + htmlErrores + "\",\n";
            jsonEnvio += "\"Simbolos\": \"\"\n";
            jsonEnvio += "}\n";
            return jsonEnvio;
        }
    }


    public static void AgregarFunciones(Tabla table, ArrayList<AST> functions) {
        for (int i = 0; i < functions.size(); i++) {
            Instruccion ins = (Instruccion) functions.get(i);
            if (ins instanceof Funcion) {
                Funcion f = (Funcion) ins;
                f.setTipo(f.getTipo().verificarUserType(table));
                table.InsertarFuncion(f);
                for (int j = 0; j < f.getParametros().size(); j++) {
                    Parametro parametro = f.getParametros().get(j);
                    parametro.setTipo(parametro.getTipo().verificarUserType(table));
                }
                f.setNombreCompleto(f.generarNombreCompleto());
                //AgregarFunciones(table, f.getFunciones());
            } else if (ins instanceof Procedimiento) {
                Procedimiento p = (Procedimiento) ins;
                table.InsertarFuncion(p);
                for (int j = 0; j < p.getParametros().size(); j++) {
                    Parametro parametro = p.getParametros().get(j);
                    parametro.setTipo(parametro.getTipo().verificarUserType(table));
                }
                p.setNombreCompleto(p.generarNombreCompleto());
                //AgregarFunciones(table, p.getFunciones());
            }
        }
    }

    public String toHtmlErrors() {
        String html = "<table class=\"table table-striped table-light\" style=\"height: 100%;\"> ";
        String encabezado = "<tr style=\"height: 40px;\">";
        String filas = "";
        encabezado += "<th scope=\"col\">No</th>";
        encabezado += "<th scope=\"col\">Descripcion</th>";
        encabezado += "<th scope=\"col\">Tipo</th>";
        encabezado += "<th scope=\"col\">Fila</th>";
        encabezado += "<th scope=\"col\">Columna</th>";
        for (int i = 0; i < errores.size(); i++) {
            Excepcion sim = errores.get(i);
            filas += "<tr>";
            filas += "<td scope=\"row\">" + i + "</td>";
            filas += "<td scope=\"row\">" + sim.getDescripcion() + "</td>";
            filas += "<td scope=\"row\">" + sim.getTipoError() + "</td>";
            filas += "<td scope=\"row\">" + sim.getLinea() + "</td>";
            filas += "<td scope=\"row\">" + sim.getColumna() + "</td>";
            filas += "</tr>";
        }
        encabezado += "</tr>";
        html += encabezado + filas;
        html += "</table>";
        return html;
    }
}
