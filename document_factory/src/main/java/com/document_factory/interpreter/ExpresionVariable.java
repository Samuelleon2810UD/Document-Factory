package com.document_factory.interpreter;

/** INTERPRETER – Expresión terminal: resuelve una variable en el Contexto. */
public class ExpresionVariable implements Expresion {

    private final String nombre;

    public ExpresionVariable(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public double interpretar(Contexto contexto) {
        Double valor = contexto.obtenerVariable(nombre);
        if (valor == null) {
            throw new IllegalStateException("Variable no definida: '" + nombre + "'");
        }
        return valor;
    }
}
