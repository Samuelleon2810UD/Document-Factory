package interpreter;

import java.util.HashMap;
import java.util.Map;

// Contexto del Interpreter: variables numéricas y variables de texto
public class Contexto {
    private final Map<String, Double> variables = new HashMap<>();
    private final Map<String, String> textos = new HashMap<>();

    public Double obtenerVariable(String nombre) {
        Double valor = variables.get(nombre);
        if (valor == null) {
            throw new ExpresionInvalidaException("Variable no definida: " + nombre);
        }
        return valor;
    }

    public void asignarVariable(String nombre, Double valor) {
        variables.put(nombre, valor);
    }

    public void asignarTexto(String nombre, String valor) {
        textos.put(nombre, valor);
    }

    public boolean tieneTexto(String nombre) {
        return textos.containsKey(nombre);
    }

    public String obtenerTexto(String nombre) {
        return textos.get(nombre);
    }
}
