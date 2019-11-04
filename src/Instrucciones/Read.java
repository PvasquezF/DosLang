package Instrucciones;

import Excepciones.Excepcion;
import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

public class Read implements Instruccion {
    private AccesoVariable variable;
    private Tipo tipo;
    private int fila;
    private int columna;

    public Read(AccesoVariable variable, int fila, int columna) {
        this.variable = variable;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        Object result = variable.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        tipo = (Tipo) result;
        return null;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "// SENTENCIA READ \n";
        codigo += variable.get4D(tabla, arbol);
        String temp2 = tabla.getTemporalActual();

        String temp1 = tabla.getTemporal();
        codigo += "+,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "+,p,0," + temp1 + "\n";
        codigo += "=," + temp1 + ",-1,stack\n";

        codigo += "+,p,1," + temp1 + "\n";
        if (tipo.getType() == Tipo.tipo.INTEGER) {
            codigo += "=," + temp1 + ",0,stack\n";
        } else if (tipo.getType() == Tipo.tipo.STRING) {
            codigo += "=," + temp1 + ",1,stack\n";
        } else if (tipo.getType() == Tipo.tipo.REAL) {
            codigo += "=," + temp1 + ",2,stack\n";
        } else if (tipo.getType() == Tipo.tipo.BOOLEAN) {
            codigo += "=," + temp1 + ",3,stack\n";
        } else if (tipo.getType() == Tipo.tipo.CHAR) {
            codigo += "=," + temp1 + ",4,stack\n";
        } else if (tipo.getType() == Tipo.tipo.WORD) {
            codigo += "=," + temp1 + ",5,stack\n";
        }

        codigo += "+,p,2," + temp1 + "\n";
        if (variable.accesoGlobal) {
            codigo += "=," + temp1 + "," + temp2 + ",stack\n";
        } else {
            String temp3 = tabla.getTemporal();
            codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + "," + temp3 + "\n";
            codigo += "+," + temp3 + "," + temp2 + "," + temp2 + "\n";
            codigo += "=," + temp1 + "," + temp2 + ",stack\n";
        }

        codigo += "+,p,3," + temp1 + "\n";
        if (variable.accesoGlobal) {
            codigo += "=," + temp1 + ",1,stack\n";
        } else {
            codigo += "=," + temp1 + ",0,stack\n";
        }
        codigo += "call,,,$_in_value\n";
        codigo += "-,p," + tabla.getTamañoActualFuncion().peek() + ",p\n";
        codigo += "// FIN SENTENCIA READ\n";
        return codigo;
    }
}
