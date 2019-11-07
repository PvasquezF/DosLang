package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

public class Sizeof implements Expresion {

    private Expresion registro;
    private int fila;
    private int columna;
    private Tipo tipo;

    public Sizeof(Expresion registro, int fila, int columna) {
        this.registro = registro;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        Object result = registro.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        tipo = (Tipo) result;
        if (tipo.getType() != Tipo.tipo.RECORD) {
            Excepcion e = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "La sentencia sizeof solo aplica para registros",
                    this.fila, this.columna);
            arbol.getErrores().add(e);
            return e;
        }
        return new Tipo(Tipo.tipo.INTEGER);
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        return null;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Sentencia sizeOf fila: " + this.fila + ", columna: " + this.columna + "\n";
        codigo += "=," + this.tipo.getAtributos().size() + ",," + tabla.getTemporal() + "\n";
        codigo += "// Fin Sentencia sizeOf\n";
        return codigo;
    }
}
