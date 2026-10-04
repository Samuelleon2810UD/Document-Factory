package com.document_factory.interpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * INTERPRETER – Contexto global del intérprete.
 * Guarda las variables (precio, unidades, ...) que las expresiones incrustadas
 * en el texto del documento (${precio * unidades}) pueden consultar.
 */
public class Contexto {

    private final Map<String, Double> variables = new HashMap<>();

    public Double obtenerVariable(String nombre) {
        return variables.get(nombre);
    }

    public void asignarVariable(String nombre, Double valor) {
        variables.put(nombre, valor);
    }
}
