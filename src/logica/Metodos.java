package logica;

import java.util.Stack;

public class Metodos {
    private  StringBuilder expresion = new StringBuilder();
    
     public void agregar(String entrada) {
        expresion.append(entrada);
    }
     
     public void agregarOperador(char operador){
         char ultimo= expresion.charAt(expresion.length()-1);
         if(expresion.length()==0){
             if(operador=='X' || operador=='÷')return;
         }else
             if(!esOperador(ultimo)&& ultimo!='(' && ultimo!= '.'){
                 expresion.append(operador);
             }
         
     }
     public void reiniciar() {
        expresion.setLength(0);
    }
     
     public void borrarUltimo(){
         if(expresion.length()>0){
             expresion.deleteCharAt(expresion.length()-1);
         }
     }

    public String getExpresion() {
        return expresion.toString();
    }
    private boolean esOperador(char c) {
        return c == '+' || c == '-' || c == 'X' || c == '÷';
    }

    private int precedencia(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case 'X':
            case '÷':
                return 2;
            default:
                return 0;
        }
    }
    
    private String convertirAPostfija(String expr) {
        StringBuilder output = new StringBuilder();
        Stack<Character> operadores = new Stack<>();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                // agregar números directamente
                output.append(c);
            } else {
                output.append(' '); // separador 
                if (c == '(') {
                    operadores.push(c);
                } else if (c == ')') {
                    while (!operadores.isEmpty() && operadores.peek() != '(') {
                        output.append(operadores.pop()).append(' '); // .append responde a output
                    }
                    operadores.pop(); // descartar '('
                } else if (esOperador(c)) {
                    while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(c)) {
                        output.append(operadores.pop()).append(' '); // .append responde a output
                    }
                    operadores.push(c);
                }
            }
        }

        while (!operadores.isEmpty()) {
            output.append(' ').append(operadores.pop());
        }

        return output.toString(); // pasa de string builder a string el postfijo
    }
    
    private double evaluarPostfija(String postfija) {
        Stack<Double> pila = new Stack<>();
        String[] tokens = postfija.trim().split("\\s+"); //cortar el String en base a " "

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                pila.push(Double.parseDouble(token));
            } else if (token.length() == 1 && esOperador(token.charAt(0))) { // no es un numero, se asume que es un operador
                double b = pila.pop();
                double a = pila.pop();
                switch (token.charAt(0)) {
                    case '+': pila.push(a + b); break;
                    case '-': pila.push(a - b); break;
                    case 'X': pila.push(a * b); break;
                    case '÷':
                        if (b == 0) throw new ArithmeticException("División por cero");
                        pila.push(a / b); break;
                }
            }
        }

        return pila.pop();
    }
    private double evaluarExpresion(String expr) { //sirve de enlace entre la GUI y logica
        return evaluarPostfija(convertirAPostfija(expr));
    }
    
     public String calcularResultado() {
        try {
            double resultado = evaluarExpresion(expresion.toString());
            expresion.setLength(0);
            expresion.append(resultado);
            return String.valueOf(resultado);
        } catch (Exception e) {
            expresion.setLength(0);
            return "Error";
        }
    }
    
}

