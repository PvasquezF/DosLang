package Instrucciones;

import Excepciones.Excepcion;
import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;

import java.util.ArrayList;

public class If implements Instruccion {
    private Expresion condicion;
    private ArrayList<AST> instruccionesIF;
    private ArrayList<AST> instruccionesELSE;
    private int fila;
    private int columna;

    public If(Expresion condicion, ArrayList<AST> instruccionesIF, ArrayList<AST> instruccionesELSE, int fila, int columna) {
        this.condicion = condicion;
        this.instruccionesIF = instruccionesIF;
        this.instruccionesELSE = instruccionesELSE;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        Object result = this.condicion.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        for (int i = 0; i < this.instruccionesIF.size(); i++) {
            AST ast = this.instruccionesIF.get(i);
            if (ast instanceof Instruccion) {
                result = ((Instruccion) ast).ejecutar(tabla, arbol);
            } else {
                result = ((Expresion) ast).getValor(tabla, arbol);
            }
            if (result instanceof Excepcion) {
                return result;
            }
        }

        for (int i = 0; i < this.instruccionesELSE.size(); i++) {
            AST ast = this.instruccionesELSE.get(i);
            if (ast instanceof Instruccion) {
                result = ((Instruccion) ast).ejecutar(tabla, arbol);
            } else {
                result = ((Expresion) ast).getValor(tabla, arbol);
            }
            if (result instanceof Excepcion) {
                return result;
            }
        }
        return null;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();
        String label3 = tabla.getEtiqueta();

        codigo += this.condicion.get4D(tabla, arbol);
        codigo += "je," + tabla.getTemporalActual() + ",1," + label1 + "\n";
        tabla.QuitarTemporal(tabla.getTemporalActual());
        codigo += "jmp,,," + label2 + "\n";
        codigo += label1 + ": // Condicion verdadera\n";
        for (int i = 0; i < this.instruccionesIF.size(); i++) {
            codigo += this.instruccionesIF.get(i).get4D(tabla, arbol);
        }
        codigo += "jmp,,," + label3 + " // Salida if\n";
        codigo += label2 + ": // Condicion falsa\n";
        for (int i = 0; i < this.instruccionesELSE.size(); i++) {
            codigo += this.instruccionesELSE.get(i).get4D(tabla, arbol);
        }
        codigo += label3 + ": // FIN IF\n";
        return codigo;
    }
}
