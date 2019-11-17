package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.*;

import java.util.ArrayList;

public class Sizeof implements Expresion {

    private Expresion registro;
    private int fila;
    private int columna;
    private Tipo tipo;
    private String fromType;

    public Sizeof(Expresion registro, int fila, int columna) {
        this.registro = registro;
        this.fila = fila;
        this.columna = columna;
    }

    public Sizeof(String fromType, int fila, int columna) {
        this.fromType = fromType;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        if (registro != null) {
            Object result = registro.getTipo(tabla, arbol);
            if (result instanceof Excepcion) {
                return result;
            }
            tipo = (Tipo) result;
        } else {
            ArrayList<UserType> uts = tabla.getEnviroment().getUserTypes();
            boolean encontro = false;
            for (UserType u : uts) {
                if (u.getNombre().equalsIgnoreCase(fromType)) {
                    tipo = u.getTipo();
                    encontro = true;
                    break;
                }
            }
            if (!encontro) {
                Object result = tabla.getVariable(fromType);
                if (result instanceof String) {
                    Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                            "No se ha encontrado el type o la variable " + fromType + ".",
                            fila, columna);
                    arbol.getErrores().add(exc);
                    return exc;
                }
                tipo = ((Simbolo) result).getTipo();
            }
        }
        if (tipo.getType() != Tipo.tipo.RECORD) {
            Excepcion e = new Excepcion(Excepcion.TIPOERROR.SEMANTICO, "La sentencia sizeof solo aplica para registros",
                    this.fila, this.columna);
            arbol.getErrores().add(e);
            return e;
        }
        return new Tipo(Tipo.tipo.INTEGER);
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// Sentencia sizeOf fila: " + this.fila + ", columna: " + this.columna + "\n";
        codigo += "=," + this.tipo.getAtributos().size() + ",," + tabla.getTemporal() + "\n";
        codigo += "// Fin Sentencia sizeOf\n";
        return codigo;
    }
}
