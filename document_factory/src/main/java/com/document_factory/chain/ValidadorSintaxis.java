package com.document_factory.chain;

import com.document_factory.bridge.Documento;

/**
 * CHAIN – Eslabón 1: valida la sintaxis de las expresiones ${...} incrustadas.
 * Va primero a propósito: protege al EvaluadorExpresiones (INTERPRETER) de
 * recibir texto malformado.
 */
public class ValidadorSintaxis extends ProcesadorHandler {

    @Override
    protected boolean ejecutarProcesamiento(Documento doc) {
        for (Documento.Seccion s : doc.getSecciones()) {
            String texto = s.getTexto();
            int pos = texto.indexOf("${");
            while (pos != -1) {
                int fin = texto.indexOf('}', pos);
                if (fin == -1) {
                    return rechazar("Falta '}' para cerrar la expresión en: \"" + texto + "\"");
                }
                String expr = texto.substring(pos + 2, fin);
                if (expr.trim().isEmpty()) {
                    return rechazar("Expresión vacía en: \"" + texto + "\"");
                }
                if (!expr.matches("[A-Za-z0-9_+*().\\s]+")) {
                    return rechazar("Caracteres no permitidos en la expresión \"" + expr + "\"");
                }
                int balance = 0;
                for (char c : expr.toCharArray()) {
                    if (c == '(') balance++;
                    if (c == ')') balance--;
                    if (balance < 0) break;
                }
                if (balance != 0) {
                    return rechazar("Paréntesis desbalanceados en la expresión \"" + expr + "\"");
                }
                pos = texto.indexOf("${", fin);
            }
        }
        return aprobar("Sintaxis correcta");
    }
}
