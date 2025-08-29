
package logica;

import calculadoraGui.CalculadoraGUI;


public class CalculadoraEjecutable {
    public static void main(String[] args) {
        CalculadoraGUI calcu = new CalculadoraGUI();
        calcu.setVisible(true);
        calcu.setLocationRelativeTo(null);
        Metodos metodos =new Metodos ();
    }
}
