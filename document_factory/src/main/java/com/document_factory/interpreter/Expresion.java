package com.document_factory.interpreter;

/**
 * INTERPRETER – Expresión abstracta.
 * Cada nodo del árbol sintáctico (número, variable, suma, multiplicación)
 * sabe interpretarse a sí mismo contra un {@link Contexto}.
 */
public interface Expresion {
    double interpretar(Contexto contexto);
}
