package logica;

import java.util.Stack;

public class Metodos {

    private StringBuilder expresion = new StringBuilder(); // Guarda lo que hay en el label de arriba
    private StringBuilder current = new StringBuilder(); // Guarda lo que hay en el label de abajo
    private boolean ultimoEsOperador = false; // bandera

    public void agregarNumero(String entrada) {
        if (ultimoEsOperador) {
            current.setLength(0); // Si al apretar un numero, el ultimo boton que se habia es un operador, se reinicia el current
            ultimoEsOperador = false; // Se actualiza la bandera
        }
        
        
        if(expresion.length()>0 && expresion.charAt(expresion.length()-1)=='='){
           reiniciarTodo(); // Si se apreta un numero y el ultimo caracter de la expresion es un igual, se tiene que reiniciar todo
        }
        
        current.append(entrada); // Se agrega un numero al current
    }

    public void agregarOperador(char operador) {
        /*char ultimo = current.charAt(current.length() - 1);
        if (current.length() == 0) {
            if (operador == 'X' || operador == '÷') {
                return;
            }
        } else if (!esOperador(ultimo) && ultimo != '(' && ultimo != '.') {
            expresion.append(current.toString()+operador);
        }*/
        
        
        if (!esOperador(operador)) return; // Si recibe algo que no es operador no sigue ejecutando
        
        if (ultimoEsOperador) {
            expresion.setLength(expresion.length()-1); // Si se presiona un operador y lo ultimo tambien habia sido operador, se lo elimina para reemplazarlo
        } else {
            expresion.append(current.toString()); // Se sube lo que habia en el current
            calcularResultado(); // Se calcula el resultado parcial
            
        }
        
        expresion.append(operador); // Se agrega el operador a la expresion
        ultimoEsOperador = true; //Se actualiza la bandera
        
        
        
        

    }

    public void reiniciarCurrent() {
        current.setLength(0);
    }
    
    public void reiniciarTodo() {
        expresion.setLength(0);
        current.setLength(0);
    }

    public void borrarUltimo() {
        if (current.length() > 0) {
            current.deleteCharAt(current.length() - 1);
        }
    }

    public String getExpresion() {
        return expresion.toString();
    }
    
    public String getCurrent() {
        return current.toString();
    }

    private boolean esOperador(char c) {
        return c == '+' || c == '-' || c == 'X' || c == '÷' || c== '=';
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
                    case '+':
                        pila.push(a + b);
                        break;
                    case '-':
                        pila.push(a - b);
                        break;
                    case 'X':
                        pila.push(a * b);
                        break;
                    case '÷':
                        if (b == 0) {
                            throw new ArithmeticException("División por cero");
                        }
                        pila.push(a / b);
                        break;
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
            current.setLength(0);
            current.append(resultado);
            return String.valueOf(resultado);
        } catch (Exception e) {
            current.setLength(0);
            return "Error";
        }
    }

}
