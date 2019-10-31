package Instrucciones;

import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;

public class Continue  implements Instruccion {

    private int fila;
    private int columna;

    public Continue(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        tabla.getSentenciasContinueActivas().push(this);
        return this;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        codigo += "jmp,,," + tabla.getEtiqueta() + "\n";
        tabla.getEtiquetasContinue().push(tabla.getEtiquetaActual());
        return codigo;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
}
