package Instrucciones;

import Excepciones.Excepcion;
import Interfaces.AST;
import Interfaces.Instruccion;
import TablaSimbolos.Simbolo;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tipo;
import TablaSimbolos.Tree;

import java.util.ArrayList;

public class Main extends Simbolo implements Instruccion {
    private ArrayList<AST> instrucciones;
    private int fila;
    private int columna;

    public Main(ArrayList<AST> instrucciones, int fila, int columna) {
        super("main", new ArrayList<>(), new Tipo(Tipo.tipo.VOID), "", "Clase principal", "Global", instrucciones, false, -1, instrucciones.size());
        this.instrucciones = instrucciones;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        String result = tabla.InsertarFuncion(this);
        if (result != null) {
            Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                    (String) result,
                    fila, columna);
            arbol.getErrores().add(exc);
            return exc;
        }
        return null;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        tabla.getTamañoActualFuncion().push(0);
        String codigo = "// Inicio main linea: " + fila + ", columna: " + columna + "\n";
        codigo += "begin,,,main\n";
        for (AST ins : this.instrucciones) {
            codigo += ins.get4D(tabla, arbol);
        }
        codigo += "end,,,main\n";
        //codigo += "=," + this.instrucciones.size() + ",," + temp1 + "\n";
        //codigo += "+,p," + temp1 + ",p\n";
        codigo += "call,,,main\n";
        //codigo += "-,p," + temp1 + ",p\n";
        codigo += "//Fin main\n";
        return codigo;
    }
}
