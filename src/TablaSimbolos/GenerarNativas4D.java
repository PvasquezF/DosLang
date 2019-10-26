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
public class GenerarNativas4D {

    public String generarConcatenacion(Tabla tabla) {
        String codigo = "";
        String temp1 = tabla.getTemporal();
        String temp2 = tabla.getTemporal();
        String temp3 = tabla.getTemporal();
        String temp4 = tabla.getTemporal();
        String temp5 = tabla.getTemporal();
        String temp6 = tabla.getTemporal();
        String temp7 = tabla.getTemporal();
        String temp8 = tabla.getTemporal();

        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();
        String label3 = tabla.getEtiqueta();
        String label4 = tabla.getEtiqueta();

        codigo += "begin,,,concatenar_cadenas\n";
        codigo += "+,p,0," + temp1 + "\n";
        codigo += "=,stack," + temp1 + "," + temp2 + "\n";
        codigo += "+,p,1," + temp3 + "\n";
        codigo += "=,stack," + temp3 + "," + temp4 + "\n";
        codigo += "=,h,," + temp7;
        codigo += label1 + ":" + "\n";
        codigo += "=,heap," + temp2 + "," + temp5 + "\n";
        codigo += "je," + temp5 + ",0," + label2 + "\n";
        codigo += "=,h," + temp5 + ",heap" + "\n";
        codigo += "+,h,1,h\n";
        codigo += "+," + temp2 + ",1," + temp2 + "\n";
        codigo += "jmp,,," + label1 + "\n";
        codigo += label2 + ":" + "\n";

        codigo += label3 + ":" + "\n";
        codigo += "=,heap," + temp4 + "," + temp6 + "\n";
        codigo += "je," + temp6 + ",0," + label4 + "\n";
        codigo += "=,h," + temp6 + ",heap" + "\n";
        codigo += "+,h,1,h\n";
        codigo += "+," + temp4 + ",1," + temp4 + "\n";
        codigo += "jmp,,," + label3 + "\n";
        codigo += label4 + ":" + "\n";
        codigo += "=,h,0,heap" + "\n";
        codigo += "+,h,1,h\n";
        codigo += "+,p,2," + temp8 + "\n";
        codigo += "=," + temp8 + "," + temp7 + ",stack\n";
        codigo += "end,,,concatenar_cadenas" + "\n";
        return codigo;
    }

    public String generarPrint(Tabla tabla) {
        String codigo = "";
        String temp1 = tabla.getTemporal();
        String temp2 = tabla.getTemporal();
        String temp3 = tabla.getTemporal();
        //String temp4 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();

        codigo += "begin,,,print_str\n";
        codigo += "+,p,0," + temp1 + "\n";
        codigo += "=,stack," + temp1 + "," + temp2 + "\n";
        codigo += label2 + ":\n";
        codigo += "=,heap," + temp2 + "," + temp3 + "\n";
        codigo += "je," + temp3 + ",0," + label1 + "\n";
        codigo += "print(%c," + temp3 + ")\n";
        codigo += "+,1," + temp2 + "," + temp2 + "\n";
        codigo += "jmp,,," + label2 + "\n";
        codigo += label1 + ":\n";
        codigo += "print(%c,13)\n";
        //codigo += "print(%c,10)\n";
        codigo += "end,,,print_str\n";
        return codigo;
    }

    public String generarTrunk(Tabla tabla) {
        String codigo = "";
        codigo += "begin,,,trunk_primitiva\n";
        String temp1 = tabla.getTemporal();
        String temp2 = tabla.getTemporal();
        String temp3 = tabla.getTemporal();
        String temp4 = tabla.getTemporal();
        String temp5 = tabla.getTemporal();
        codigo += "+,p,0," + temp1 + "\n";
        codigo += "=,stack," + temp1 + "," + temp2 + "\n";
        codigo += "%," + temp2 + ",1," + temp3 + "\n";
        codigo += "-," + temp2 + "," + temp3 + "," + temp4 + "\n";
        codigo += "+,p,1," + temp5 + "\n";
        codigo += "=," + temp5 + "," + temp4 + ",stack\n";
        codigo += "end,,,trunk_primitiva\n";
        return codigo;
    }

    public String generarRound(Tabla tabla) {
        String codigo = "";
        codigo += "begin,,,round_primitiva\n";
        String temp1 = tabla.getTemporal();
        String temp2 = tabla.getTemporal();
        String temp3 = tabla.getTemporal();
        String temp4 = tabla.getTemporal();
        String temp5 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        codigo += "+,p,0," + temp1 + "\n";
        codigo += "=,stack," + temp1 + "," + temp2 + "\n";
        codigo += "%," + temp2 + ",1," + temp3 + "\n";
        codigo += "-," + temp2 + "," + temp3 + "," + temp4 + "\n";
        codigo += "jl," + temp3 + ",0.5," + label1 + "\n";
        codigo += "+," + temp4 + ",1," + temp4 + "\n";
        codigo += label1 + ":\n";
        codigo += "+,p,1," + temp5 + "\n";
        codigo += "=," + temp5 + "," + temp4 + ",stack\n";
        codigo += "end,,,round_primitiva\n";
        return codigo;
    }

    public String generarChartAt(Tabla tabla) {
        String codigo = "";
        String temp1 = tabla.getTemporal();
        String temp2 = tabla.getTemporal();
        String temp3 = tabla.getTemporal();
        String temp4 = tabla.getTemporal();
        String temp5 = tabla.getTemporal();
        String temp6 = tabla.getTemporal();
        String temp7 = tabla.getTemporal();

        codigo += "begin,,,CharAt_primitiva\n";
        codigo += "+,p,0," + temp1 + "\n";
        codigo += "=,stack," + temp1 + "," + temp2 + "\n";
        codigo += "+,p,1," + temp3 + "\n";
        codigo += "=,stack," + temp3 + "," + temp4 + "\n";
        codigo += "+," + temp2 + ", " + temp4 + "," + temp5 + "\n";
        codigo += "=,heap," + temp5 + "," + temp6 + "\n";
        codigo += "+,p,2," + temp7 + "\n";
        codigo += "=," + temp7 + "," + temp6 + ",stack\n";
        codigo += "end,,,CharAt_primitiva" + "\n";
        return codigo;
    }

    public String generarLenght(Tabla tabla) {
        String codigo = "";
        String temp1 = tabla.getTemporal();
        String temp2 = tabla.getTemporal();
        String temp3 = tabla.getTemporal();
        String temp4 = tabla.getTemporal();
        String temp5 = tabla.getTemporal();
        String label1 = tabla.getEtiqueta();
        String label2 = tabla.getEtiqueta();

        codigo += "begin,,,lenght_primitiva\n";
        codigo += "+,p,0," + temp1 + "\n";
        codigo += "=,stack," + temp1 + "," + temp2 + "\n";
        codigo += "=,0,," + temp3 + "\n";
        codigo += label2 + ":\n";
        codigo += "=,heap," + temp2 + "," + temp4 + "\n";
        codigo += "je," + temp4 + ",0," + label1 + "\n";
        codigo += "+,1," + temp3 + "," + temp3 + "\n";
        codigo += "+,1," + temp2 + "," + temp2 + "\n";
        codigo += "jmp,,," + label2 + "\n";
        codigo += label1 + ":\n";
        codigo += "+,p,1," + temp5 + "\n";
        codigo += "=,"+temp5+"," + temp3 + ",stack\n";
        codigo += "end,,,lenght_primitiva\n";
        return codigo;
    }

    public String generarRangoFueraLimites(Tabla tabla) {
        String codigo = "";
        codigo += "begin,,,rango_sobrepasado\n";
        codigo += "print(%c,86)\n";
        codigo += "print(%c,97)\n";
        codigo += "print(%c,108)\n";
        codigo += "print(%c,111)\n";
        codigo += "print(%c,114)\n";
        codigo += "print(%c,32)\n";
        codigo += "print(%c,102)\n";
        codigo += "print(%c,117)\n";
        codigo += "print(%c,101)\n";
        codigo += "print(%c,114)\n";
        codigo += "print(%c,97)\n";
        codigo += "print(%c,32)\n";
        codigo += "print(%c,100)\n";
        codigo += "print(%c,101)\n";
        codigo += "print(%c,32)\n";
        codigo += "print(%c,108)\n";
        codigo += "print(%c,111)\n";
        codigo += "print(%c,115)\n";
        codigo += "print(%c,32)\n";
        codigo += "print(%c,108)\n";
        codigo += "print(%c,105)\n";
        codigo += "print(%c,109)\n";
        codigo += "print(%c,105)\n";
        codigo += "print(%c,116)\n";
        codigo += "print(%c,101)\n";
        codigo += "print(%c,115)\n";
        codigo += "print(%c,32)\n";
        codigo += "print(%c,100)\n";
        codigo += "print(%c,101)\n";
        codigo += "print(%c,108)\n";
        codigo += "print(%c,32)\n";
        codigo += "print(%c,114)\n";
        codigo += "print(%c,97)\n";
        codigo += "print(%c,110)\n";
        codigo += "print(%c,103)\n";
        codigo += "print(%c,111)\n";
        codigo += "print(%c,46)\n";
        codigo += "print(%c,10)\n";
        codigo += "end,,,rango_sobrepasado\n";
        return codigo;
    }
}
//86 97 108 111 114 32 102 117 101 114 97 32 100 101 32 108 111 115 32 108 105 109 105 116 101 115 32 100 101 108 32 114 97 110 103 111 46
