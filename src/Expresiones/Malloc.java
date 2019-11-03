package Expresiones;

import Excepciones.Excepcion;
import Interfaces.Expresion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

public class Malloc implements Expresion {
    private Expresion cantidad;
    private int fila;
    private int columna;

    public Malloc(Expresion cantidad, int fila, int columna) {
        this.cantidad = cantidad;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object getTipo(Tabla tabla, Tree arbol) {
        Object result = cantidad.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        Tipo tipoCantidad = (Tipo) result;
        if (tipoCantidad.getType() != Tipo.tipo.INTEGER) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    "Se esperaba un valor entero para reservar el espacio.",
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        return new Tipo(Tipo.tipo.RECORD, true);
    }

    @Override
    public Object getValor(Tabla tabla, Tree arbol) {
        return null;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        String temp1 = tabla.getTemporal();
        String temp2 = tabla.getTemporal();
        String temp3 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();
        codigo += cantidad.get4D(tabla, arbol);
        codigo += "=," + tabla.getTemporalActual() + ",," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        tabla.QuitarTemporal(tabla.getTemporalActual());
        codigo += "=,0,," + temp2 + "\n";
        tabla.AgregarTemporal(temp2);
        codigo += "=,h,," + temp3 + "\n";
        tabla.AgregarTemporal(temp3);
        codigo += label1 + ":\n";
        codigo += "jge," + temp2 + "," + temp1 + "," + label2 + "\n";
        tabla.QuitarTemporal(temp2);
        tabla.QuitarTemporal(temp1);
        codigo += "=,h,-1,heap\n";
        codigo += "+,h,1,h\n";
        codigo += "+," + temp2 + ",1," + temp2 + "\n";
        tabla.AgregarTemporal(temp2);
        codigo += "jmp,,," + label1 + "\n";
        codigo += label2 + ":\n";
        codigo += "=," + temp3 + ",," + tabla.getTemporal() + "\n";
        tabla.AgregarTemporal(tabla.getTemporalActual());
        tabla.QuitarTemporal(temp3);
        return codigo;
    }
}
