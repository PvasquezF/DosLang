package Instrucciones;

import Excepciones.Excepcion;
import Expresiones.Acceso;
import Expresiones.Identificador;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

import java.util.ArrayList;

public class Write implements Instruccion {
    private ArrayList<Expresion> valores;
    private int fila;
    private int columna;

    public Write(ArrayList<Expresion> valor, int fila, int columna) {
        this.valores = valor;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        return null;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        for (Expresion valor : valores) {
            Object result = valor.getTipo(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            Tipo tipo = (Tipo) result;
            codigo += valor.get4D(tabla, arbol);
            String temp = tabla.getTemporalActual();
            if (tipo.getType() == Tipo.tipo.INTEGER) {
                codigo += "print(%e," + temp + ")\n";
            } else if (tipo.getType() == Tipo.tipo.REAL) {
                codigo += "print(%d," + temp + ")\n";
            } else if (tipo.getType() == Tipo.tipo.BOOLEAN) {
                String label1 = tabla.getEtiqueta();
                String label2 = tabla.getEtiqueta();
                codigo += "je,0," + temp + "," + label1 + "\n";
                codigo += "print(%c,116)\n";
                codigo += "print(%c,114)\n";
                codigo += "print(%c,117)\n";
                codigo += "print(%c,101)\n";
                codigo += "jmp,,," + label2 + "\n";
                codigo += label1 + ":\n";
                codigo += "print(%c,102)\n";
                codigo += "print(%c,97)\n";
                codigo += "print(%c,108)\n";
                codigo += "print(%c,115)\n";
                codigo += "print(%c,101)\n";
                codigo += label2 + ":\n";
            } else if (tipo.getType() == Tipo.tipo.RANGE) {
                if (tipo.getTipoRange() == Tipo.tipo.INTEGER) {
                    codigo += "print(%e," + temp + ")\n";
                } else if (tipo.getTipoRange() == Tipo.tipo.REAL) {
                    codigo += "print(%d," + temp + ")\n";
                } else if (tipo.getTipoRange() == Tipo.tipo.CHAR) {
                    codigo += "print(%c," + temp + ")\n";
                }
            } else if (tipo.getType() == Tipo.tipo.CHAR) {
                codigo += "print(%c," + temp + ")\n";
            } else if (tipo.getType() == Tipo.tipo.ENUMERADO) {
                ArrayList<Expresion> ids = tipo.getIdentificadores();
                String labelSalida = tabla.getEtiqueta();
                for (int i = 0; i < ids.size(); i++) {
                    String label1 = tabla.getEtiqueta();
                    Identificador id = (Identificador) ((Acceso) ids.get(i)).getAccesos().get(0);
                    codigo += "jne," + temp + "," + i + "," + label1 + "\n";
                    for (int j = 0; j < id.getIdentificador().length(); j++) {
                        codigo += "print(%c," + ((int) id.getIdentificador().charAt(j)) + ")\n";
                    }
                    codigo += "jmp,,," + labelSalida + "\n";
                    codigo += label1 + ":\n";
                }
                codigo += labelSalida + ":\n";
            } else if (tipo.getType() == Tipo.tipo.STRING || tipo.getType() == Tipo.tipo.WORD) {
                String temp3 = tabla.getTemporal();
                String label1 = tabla.getEtiqueta();
                String label2 = tabla.getEtiqueta();
                codigo += label2 + ":\n";
                codigo += "=,heap," + temp + "," + temp3 + "\n";
                codigo += "je," + temp3 + ",0," + label1 + "\n";
                codigo += "print(%c," + temp3 + ")\n";
                codigo += "+,1," + temp + "," + temp + "\n";
                codigo += "jmp,,," + label2 + "\n";
                codigo += label1 + ":\n";
                //codigo += "print(%c,13)\n";
            } else if (tipo.getType() == Tipo.tipo.NIL) {
                codigo += "print(%c,110)\n";
                codigo += "print(%c,105)\n";
                codigo += "print(%c,108)\n";
            } else if (tipo.getType() == Tipo.tipo.RECORD) {
                codigo += "print(%c,68)\n";
                codigo += "print(%c,111)\n";
                codigo += "print(%c,115)\n";

                codigo += "print(%c,108)\n";
                codigo += "print(%c,97)\n";
                codigo += "print(%c,110)\n";
                codigo += "print(%c,103)\n";
                codigo += "print(%c,46)\n";
                for (int j = 0; j < tipo.getTipoObjeto().length(); j++) {
                    codigo += "print(%c," + ((int) tipo.getTipoObjeto().charAt(j)) + ")\n";
                }
            }
        }
        return codigo;
    }
}
