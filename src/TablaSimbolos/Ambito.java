package TablaSimbolos;

import java.util.ArrayList;

public class Ambito {
    private ArrayList<Simbolo> funciones;
    private ArrayList<Simbolo> procedimientos;
    private ArrayList<Simbolo> variables;
    private ArrayList<UserType> listaTipos;
    private Ambito Anterior;
    private int posicionStack;

    public Ambito(Ambito Anterior) {
        this.Anterior = Anterior;
        this.funciones = new ArrayList<>();
        this.procedimientos = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.listaTipos = new ArrayList<>();
    }

    public String insertarVariable(Simbolo simbolo) {
        Ambito aux = this;
        //while (aux != null) {
        for (int i = 0; i < aux.getVariables().size(); i++) {
            Simbolo sim = aux.getVariables().get(i);
            if (sim.getNombre().equalsIgnoreCase(simbolo.getNombre())) {
                return "La variable " + simbolo.getNombre() + " ya ha sido declarada.";
            }
        }
        //    aux = aux.getAnterior();
        //}
        this.variables.add(simbolo);
        return null;
    }

    public String insertarFuncion(Simbolo simbolo) {
        Ambito aux = this;
        //while (aux != null) {
        for (int i = 0; i < aux.getFunciones().size(); i++) {
            Simbolo sim = aux.getFunciones().get(i);
            if (sim.getNombreCompleto().equalsIgnoreCase(simbolo.getNombreCompleto())) {
                return "La funcion " + simbolo.getNombre() + " ya ha sido declarada.";
            }
        }
        //aux = aux.getAnterior();
        //}
        this.funciones.add(simbolo);
        return null;
    }

    /*public String insertarProcedimiento(Simbolo simbolo) {
        Ambito aux = this;
        //while (aux != null) {
        for (int i = 0; i < aux.getProcedimientos().size(); i++) {
            Simbolo sim = aux.getProcedimientos().get(i);
            if (sim.getNombreCompleto().equalsIgnoreCase(simbolo.getNombreCompleto())) {
                return "El procedimiento " + simbolo.getNombre() + " ya ha sido declarado.";
            }
        }
        //    aux = aux.getAnterior();
        //}
        this.procedimientos.add(simbolo);
        return null;
    }*/

    public Object getVariable(String identificador) {
        Ambito aux = this;
        while (aux != null) {
            for (int i = 0; i < aux.getVariables().size(); i++) {
                Simbolo sim = aux.getVariables().get(i);
                if (sim.getNombre().equalsIgnoreCase(identificador)) {
                    return sim;
                }
            }
            aux = aux.getAnterior();
        }
        return "No se ha encontrado la variable " + identificador + ".";
    }

    public Object getFuncion(String identificador) {
        Ambito aux = this;
        while (aux != null) {
            for (int i = 0; i < aux.getFunciones().size(); i++) {
                Simbolo sim = aux.getFunciones().get(i);
                if (sim.getNombreCompleto().equalsIgnoreCase(identificador)) {
                    return sim;
                }
            }
            aux = aux.getAnterior();
        }
        return "No se ha encontrado la funcion " + identificador + ".";
    }

    public Object getProcedimiento(String identificador) {
        Ambito aux = this;
        while (aux != null) {
            for (int i = 0; i < aux.getProcedimientos().size(); i++) {
                Simbolo sim = aux.getProcedimientos().get(i);
                if (sim.getNombreCompleto().equalsIgnoreCase(identificador)) {
                    return sim;
                }
            }
            aux = aux.getAnterior();
        }
        return "No se ha encontrado el procedimiento " + identificador + ".";
    }

    public String insertarType(UserType usertype) {
        Ambito aux = this;
        while (aux != null) {
            for (UserType ut : aux.getListaTipos()) {
                if (ut.getNombre().equalsIgnoreCase(usertype.getNombre())) {
                    return "El type " + usertype.getNombre() + " ya existe.";
                }
            }
            aux = aux.getAnterior();
        }
        this.listaTipos.add(usertype);
        return null;
    }

    public ArrayList<UserType> getUserTypes() {
        Ambito aux = this;
        Ambito ultima = this;
        while (aux != null) {
            if (aux.getAnterior() == null) {
                ultima = aux;
            }
            aux = aux.getAnterior();
        }
        return ultima.listaTipos;
    }

    public void concatenarTablas(ArrayList<Simbolo> tabla) {
        for (int i = 0; i < this.getVariables().size(); i++) {
            Simbolo sim = this.getVariables().get(i);
            tabla.add(sim);
        }
        for (int i = 0; i < this.getFunciones().size(); i++) {
            Simbolo sim = this.getFunciones().get(i);
            tabla.add(sim);
            if (sim.getEntorno() != null) {
                sim.getEntorno().concatenarTablas(tabla);
            }
        }
    }

    public ArrayList<UserType> getListaTipos() {
        return listaTipos;
    }

    public void setListaTipos(ArrayList<UserType> listaTipos) {
        this.listaTipos = listaTipos;
    }

    public ArrayList<Simbolo> getFunciones() {
        return funciones;
    }

    public void setFunciones(ArrayList<Simbolo> funciones) {
        this.funciones = funciones;
    }

    public ArrayList<Simbolo> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<Simbolo> variables) {
        this.variables = variables;
    }

    public Ambito getAnterior() {
        return Anterior;
    }

    public void setAnterior(Ambito anterior) {
        Anterior = anterior;
    }

    public int getPosicionStack() {
        return posicionStack++;
    }

    public void setPosicionStack(int posicionStack) {
        this.posicionStack = posicionStack;
    }

    public ArrayList<Simbolo> getProcedimientos() {
        return procedimientos;
    }

    public void setProcedimientos(ArrayList<Simbolo> procedimientos) {
        this.procedimientos = procedimientos;
    }
}
