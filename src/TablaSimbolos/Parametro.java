package TablaSimbolos;

import Excepciones.Excepcion;
import Interfaces.Instruccion;

import java.util.ArrayList;

public class Parametro implements Instruccion {
    private Tipo tipo;
    private ArrayList<String> identificador;
    private boolean referencia;
    private int fila;
    private int columna;

    public Parametro(Tipo tipo, ArrayList<String> identificador, boolean referencia, int fila, int columna) {
        this.tipo = tipo;
        this.identificador = identificador;
        this.referencia = referencia;
        this.fila = fila;
        this.columna = columna;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public ArrayList<String> getIdentificador() {
        return identificador;
    }

    public void setIdentificador(ArrayList<String> identificador) {
        this.identificador = identificador;
    }

    public boolean isReferencia() {
        return referencia;
    }

    public void setReferencia(boolean referencia) {
        this.referencia = referencia;
    }

    @Override
    public Object ejecutar(Tabla tabla, Tree arbol) {
        for (int i = 0; i < identificador.size(); i++) {
            Simbolo simbolo = null;
            String nombre = identificador.get(i);
            Tipo tipoAux = tipo.verificarUserType(tabla);
            simbolo = new Simbolo(nombre, tipoAux, tabla.getAmbito(), "parametro", "local", Tipo.valorPredeterminado(tipoAux), false, tabla.getEnviroment().getPosicionStack(), referencia);
            tabla.getEnviroment().getPosicionStack();
            Object result = tabla.InsertarVariable(simbolo);
            if (result != null) {
                Excepcion exc = new Excepcion(Excepcion.TIPOERROR.SEMANTICO,
                        (String) result,
                        fila, columna);
                arbol.getErrores().add(exc);
                return exc;
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
        return null;
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
