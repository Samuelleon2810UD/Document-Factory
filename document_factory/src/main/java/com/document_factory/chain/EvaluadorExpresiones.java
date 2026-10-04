package com.document_factory.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.document_factory.flyweight.ElementoVisual;
import com.document_factory.flyweight.FlyweightFactory;
import com.document_factory.interpreter.Contexto;
import com.document_factory.interpreter.Expresion;
import com.document_factory.interpreter.ExpresionMultiplicacion;
import com.document_factory.interpreter.ExpresionNumero;
import com.document_factory.interpreter.ExpresionSuma;
import com.document_factory.interpreter.ExpresionVariable;

import com.document_factory.bridge.Documento.Seccion;

import com.document_factory.bridge.Documento;

/**
 * CHAIN – Eslabón 3 (último): es el PUENTE entre la cadena y el INTERPRETER.
 *
 * COMBINACIÓN DE PATRONES (la más rica del proyecto):
 *  - CHAIN:       este eslabón solo se ejecuta si el sintáctico y el filtro aprobaron.
 *  - INTERPRETER: convierte el texto "${(precio + 10) * unidades}" en un árbol de
 *                 Expresion y lo interpreta contra el Contexto.
 *  - FLYWEIGHT:   al cambiar el texto, se regeneran los ElementoVisual reutilizando
 *                 los glifos compartidos de la FlyweightFactory.
 *  - (Documento pertenece al BRIDGE: se modifica sin saber nada del formato de salida.)
 */
public class EvaluadorExpresiones extends ProcesadorHandler {

    private static final Pattern EXPRESION = Pattern.compile("\\$\\{([^}]*)\\}");

    private final Contexto contexto;
    private final FlyweightFactory fabrica; // extensión sobre el diagrama, necesaria para regenerar glifos

    public EvaluadorExpresiones(Contexto contexto, FlyweightFactory fabrica) {
        this.contexto = contexto;
        this.fabrica = fabrica;
    }

    @Override
    protected boolean ejecutarProcesamiento(Documento doc) {
        int evaluadas = 0;
        // Copia: reemplazarTexto modifica las secciones, no la lista, pero así es más seguro.
        for (Documento.Seccion s : new ArrayList<>(doc.getSecciones())) {
            Matcher m = EXPRESION.matcher(s.getTexto());
            StringBuffer nuevo = new StringBuffer();
            boolean huboCambios = false;
            while (m.find()) {
                try {
                    Expresion arbol = new Parser(m.group(1)).parsear();   // texto -> árbol (Interpreter)
                    double valor = arbol.interpretar(contexto);           // árbol -> resultado
                    m.appendReplacement(nuevo, Matcher.quoteReplacement(formatear(valor)));
                    huboCambios = true;
                    evaluadas++;
                } catch (RuntimeException ex) {
                    return rechazar("No se pudo evaluar \"" + m.group(0) + "\": " + ex.getMessage());
                }
            }
            if (huboCambios) {
                m.appendTail(nuevo);
                regenerarGlifos(doc, s, nuevo.toString());
            }
        }
        return aprobar(evaluadas + " expresión(es) evaluada(s)");
    }

    /** FLYWEIGHT: reconstruye los elementos visuales de la sección con el mismo estilo. */
    private void regenerarGlifos(Documento doc, Seccion s, String nuevoTexto) {
        int x = 10, y = 0;
        String color = "#000000";
        float escala = 1.0f;
        if (!s.getElementos().isEmpty()) {
            ElementoVisual primero = s.getElementos().get(0);
            x = primero.getPosX();
            y = primero.getPosY();
            color = primero.getColor();
            escala = primero.getEscala();
        }
        List<ElementoVisual> nuevos = fabrica.crearTexto(nuevoTexto, "Georgia", x, y, color, escala);
        doc.reemplazarTexto(s, nuevoTexto, nuevos);
    }

    private static String formatear(double v) {
        return v == Math.rint(v) ? String.valueOf((long) v) : String.valueOf(v);
    }

    /**
     * Parser descendente recursivo (precedencia: * sobre +).
     *   expresion := termino ('+' termino)*
     *   termino   := factor ('*' factor)*
     *   factor    := numero | variable | '(' expresion ')'
     * Construye los nodos del INTERPRETER.
     */
    private static final class Parser {
        private final String s;
        private int i = 0;

        Parser(String s) {
            this.s = s;
        }

        Expresion parsear() {
            Expresion e = expresion();
            saltar();
            if (i < s.length()) {
                throw new IllegalArgumentException("Texto inesperado '" + s.substring(i) + "'");
            }
            return e;
        }

        private Expresion expresion() {
            Expresion izq = termino();
            while (consumir('+')) {
                izq = new ExpresionSuma(izq, termino());
            }
            return izq;
        }

        private Expresion termino() {
            Expresion izq = factor();
            while (consumir('*')) {
                izq = new ExpresionMultiplicacion(izq, factor());
            }
            return izq;
        }

        private Expresion factor() {
            saltar();
            if (i >= s.length()) {
                throw new IllegalArgumentException("Expresión incompleta");
            }
            char c = s.charAt(i);
            if (c == '(') {
                i++;
                Expresion e = expresion();
                if (!consumir(')')) {
                    throw new IllegalArgumentException("Falta ')'");
                }
                return e;
            }
            if (Character.isDigit(c) || c == '.') {
                int ini = i;
                while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) i++;
                return new ExpresionNumero(Double.parseDouble(s.substring(ini, i)));
            }
            if (Character.isLetter(c) || c == '_') {
                int ini = i;
                while (i < s.length() && (Character.isLetterOrDigit(s.charAt(i)) || s.charAt(i) == '_')) i++;
                return new ExpresionVariable(s.substring(ini, i));
            }
            throw new IllegalArgumentException("Símbolo inesperado '" + c + "'");
        }

        private boolean consumir(char esperado) {
            saltar();
            if (i < s.length() && s.charAt(i) == esperado) {
                i++;
                return true;
            }
            return false;
        }

        private void saltar() {
            while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++;
        }
    }
}
