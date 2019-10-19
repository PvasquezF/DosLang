/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TablaSimbolos;

/**
 *
 * @author Pavel
 */
public class ReservarMemoria {

    public static String Reservar(Tabla tabla, int Espacios) {
        String codigo = "";
        int index = 0;
        while (Espacios != index) {
            codigo += "=, " + index + ", -1, heap\n";
            codigo += "+, h, 1, h\n";
            index++;
        }
        return codigo;
    }
}
