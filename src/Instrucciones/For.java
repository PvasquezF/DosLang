package Instrucciones;

import Excepciones.Excepcion;
import Expresiones.Acceso;
import Expresiones.Identificador;
import Expresiones.Operacion;
import Expresiones.Primitivo;
import Interfaces.AST;
import Interfaces.Expresion;
import Interfaces.Instruccion;
import TablaSimbolos.Tabla;
import TablaSimbolos.Tree;

import java.util.ArrayList;
import java.util.Optional;

public class For implements Instruccion {
    private AST asignacion;
    private Asignacion Aumento;
    private Expresion condicion;
    private ArrayList<AST> instrucciones;
    private boolean aumento;
    private int fila;
    private int columna;

    public For(AST asignacion, Expresion condicion, ArrayList<AST> instrucciones, boolean aumento, int fila, int columna) {
        this.asignacion = asignacion;
        this.condicion = condicion;
        this.instrucciones = instrucciones;
        this.aumento = aumento;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        Object result = ((Asignacion) this.asignacion).ejecutar(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }
        result = this.condicion.getTipo(tabla, arbol);
        if (result instanceof Excepcion) {
            return result;
        }

        for (int i = 0; i < this.instrucciones.size(); i++) {
            AST ast = this.instrucciones.get(i);
            if (ast instanceof Instruccion) {
                result = ((Instruccion) ast).ejecutar(tabla, arbol);
            } else {
                result = ((Expresion) ast).getValor(tabla, arbol);
            }
            if (result instanceof Excepcion) {
                return result;
            }
        }

        Asignacion a = ((Asignacion) asignacion);
        Identificador identificador = (Identificador) a.variable.getAccesos().get(0);
        Identificador identificadorAux = new Identificador(identificador.getIdentificador(), true, fila, columna);
        ArrayList<Expresion> ListaExp = new ArrayList<>();
        ListaExp.add(identificadorAux);
        Acceso acceso = new Acceso(ListaExp, fila, columna);
        Operacion op = null;
        if (aumento) {
            op = new Operacion(acceso, new Primitivo(1), Operacion.Operador.SUMA, fila, columna);
            this.condicion = new Operacion(acceso, condicion, Operacion.Operador.MENOR_IGUAL, fila, columna);
        } else {
            op = new Operacion(acceso, new Primitivo(1), Operacion.Operador.RESTA, fila, columna);
            this.condicion = new Operacion(acceso, condicion, Operacion.Operador.MAYOR_IGUAL, fila, columna);
        }
        Aumento = new Asignacion(a.variable, op, fila, columna);
        tabla.getSentenciasBreakActivas().clear();
        tabla.getSentenciasContinueActivas().clear();
        return null;
    }

    @Override
    public int getEspacios(int espacios) {
        return 0;
    }

    @Override
    public Object get4D(Tabla tabla, Tree arbol) {
        String codigo = "";
        String temp1 = tabla.getTemporal();
        String temp2 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();
        codigo += asignacion.get4D(tabla, arbol);
        codigo += ((Asignacion) asignacion).valor.get4D(tabla, arbol);
        codigo += "=," + tabla.getTemporalActual() + ",," + temp2 + "\n";
        tabla.AgregarTemporal(temp2);
        tabla.QuitarTemporal(tabla.getTemporalActual());
        codigo += label1 + ":\n";
        codigo += condicion.get4D(tabla, arbol);
        codigo += "=," + tabla.getTemporalActual() + ",," + temp1 + "\n";
        tabla.AgregarTemporal(temp1);
        tabla.QuitarTemporal(tabla.getTemporalActual());
        codigo += "je," + temp1 + ",0," + label2 + "\n";
        tabla.QuitarTemporal(temp1);
        for (int i = 0; i < this.instrucciones.size(); i++) {
            AST ast = this.instrucciones.get(i);
            codigo += ast.get4D(tabla, arbol);
        }
        codigo += Aumento.get4D(tabla, arbol);
        for (Object o : tabla.getEtiquetasContinue()) {
            codigo += (String) o + ": // Continue\n";
        }
        codigo += "jmp,,," + label1 + "\n";
        codigo += label2 + ":\n";
        for (Object o : tabla.getEtiquetasBreak()) {
            codigo += (String) o + ": // Break\n";
        }
        tabla.getEtiquetasBreak().clear();
        tabla.getEtiquetasContinue().clear();
        return codigo;
    }
}
