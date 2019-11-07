package Instrucciones;

import Excepciones.Excepcion;
import Expresiones.Acceso;
import Expresiones.Nil;
import Expresiones.Primitivo;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

public class Free implements Instruccion {
    private AccesoVariable registro;
    private int fila;
    private int columna;
    private Tipo tipo;
    private Asignacion asignacion;

    public Free(AccesoVariable registro, int fila, int columna) {
        this.registro = registro;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        Object result = registro.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        tipo = (Tipo) result;
        if (tipo.getType() != Tipo.tipo.RECORD) {
            Excepcion e = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "La sentencia free solo aplica para registros",
                    this.fila, this.columna);
            arbol.getErrores().add(e);
            return e;
        }

        asignacion = new Asignacion(registro, new Primitivo(new Nil()), this.fila, this.columna);
        result = asignacion.ejecutar(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        return null;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Sentencia free fila: " + this.fila + ", columna: " + this.columna + "\n";
        codigo += asignacion.get4D(tabla, arbol);
        codigo += "// Fin Sentencia free\n";
        return codigo;
    }
}