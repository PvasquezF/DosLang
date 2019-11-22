package Instrucciones;

import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import LexicoDosLang.Lexer;
import SintacticoDosLang.Syntax;
import TablaSimbolos.Parametro;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Uses implements Instruccion {
    private ArrayList<String> programas;
    private int fila;
    private int columna;
    private Tree t;

    public Uses(ArrayList<String> programas, int fila, int columna) {
        this.programas = programas;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        for (String archivo : this.programas) {
            String path = Paths.get("").toAbsolutePath().toString() + "/imports/" + archivo + ".doslang";
            try {
                Lexer lexer = new Lexer(new BufferedReader(new FileReader(path)));
                Syntax s = new Syntax(lexer);
                s.parse();
                t = s.getArbol();
                for (int i = 0; i < t.getInstrucciones().size(); i++) {
                    Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
                    if (ins instanceof DeclaracionType) {
                        ins.ejecutar(tabla, t);
                    }
                }
                for (int i = 0; i < t.getInstrucciones().size(); i++) {
                    Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
                    if (ins instanceof Funcion) {
                        Funcion f = (Funcion) ins;
                        tabla.InsertarFuncion(f);
                        for (int j = 0; j < f.getParametros().size(); j++) {
                            Parametro parametro = f.getParametros().get(j);
                            parametro.setTipo(parametro.getTipo().verificarUserType(tabla));
                        }
                        f.setNombreCompleto(f.generarNombreCompleto());
                    } else if (ins instanceof Procedimiento) {
                        Procedimiento p = (Procedimiento) ins;
                        tabla.InsertarFuncion(p);
                        for (int j = 0; j < p.getParametros().size(); j++) {
                            Parametro parametro = p.getParametros().get(j);
                            parametro.setTipo(parametro.getTipo().verificarUserType(tabla));
                        }
                        p.setNombreCompleto(p.generarNombreCompleto());
                    }
                }

                for (int i = 0; i < t.getInstrucciones().size(); i++) {
                    AST ins = t.getInstrucciones().get(i);
                    if (!(ins instanceof Main)) {
                        if (ins instanceof Instruccion) {
                            if(!(ins instanceof DeclaracionType)) {
                                ((Instruccion) ins).ejecutar(tabla, t);
                            }
                        } else {
                            ((Expresion) ins).getTipo(tabla, t);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public int getEspacios(int espacios) {
        int espaciosReservaHeap = espacios;
        for (int i = 0; i < t.getInstrucciones().size(); i++) {
            Instruccion ins = (Instruccion) t.getInstrucciones().get(i);
            if (ins instanceof DeclaracionConstante
                    || ins instanceof DeclaracionVar
                    || ins instanceof DeclaracionType
                    || ins instanceof Uses) {
                espaciosReservaHeap = ins.getEspacios(espaciosReservaHeap);
            }
        }
        return espaciosReservaHeap;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        for (int i = 0; i < t.getInstrucciones().size(); i++) {
            AST ins = t.getInstrucciones().get(i);
            if (!(ins instanceof Main)) {
                codigo += ins.get4D(tabla, t);
            }
        }
        return codigo;
    }
}
