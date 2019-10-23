package TablaSimbolos;

public class Registro {
    private Tipo tipo;
    private String identificador;

    public Registro(Tipo tipo, String identificador){
        this.tipo = tipo;
        this.identificador = identificador;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
