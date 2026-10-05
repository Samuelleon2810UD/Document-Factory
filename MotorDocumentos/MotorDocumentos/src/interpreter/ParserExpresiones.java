package interpreter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Convierte el texto de una fórmula en un árbol de Expresion.
 */
public class ParserExpresiones {
    private final String src;
    private int pos;

    private ParserExpresiones(String src) {
        this.src = src;
    }

    public static Expresion parsear(String formula) {
        ParserExpresiones p = new ParserExpresiones(formula);
        Expresion e = p.expr();
        p.saltar();
        if (p.pos < p.src.length()) {
            throw new ExpresionInvalidaException(
                    "Carácter inesperado '" + p.src.charAt(p.pos) + "' en: " + formula);
        }
        return e;
    }

    /** Evalúa un marcador: variable de texto (FECHA_ACTUAL) o fórmula numérica. */
    public static String evaluarComoTexto(String formula, Contexto ctx) {
        String f = formula.trim();
        if (ctx.tieneTexto(f)) {
            return ctx.obtenerTexto(f);
        }
        return formatear(parsear(f).interpretar(ctx));
    }

    private static String formatear(double v) {
        return new BigDecimal(v).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    private Expresion expr() {
        Expresion izq = term();
        while (true) {
            saltar();
            if (consumir('+')) izq = new ExpresionSuma(izq, term());
            else if (consumir('-')) izq = new ExpresionResta(izq, term());
            else return izq;
        }
    }

    private Expresion term() {
        Expresion izq = factor();
        while (true) {
            saltar();
            if (consumir('*')) izq = new ExpresionMultiplicacion(izq, factor());
            else return izq;
        }
    }

    private Expresion factor() {
        saltar();
        if (pos >= src.length()) {
            throw new ExpresionInvalidaException("Expresión incompleta: " + src);
        }
        char c = src.charAt(pos);
        if (c == '(') {
            pos++;
            Expresion e = expr();
            saltar();
            if (!consumir(')')) throw new ExpresionInvalidaException("Falta ')' en: " + src);
            return e;
        }
        if (Character.isDigit(c) || c == '.') {
            int ini = pos;
            while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.')) pos++;
            try {
                return new ExpresionNumero(Double.parseDouble(src.substring(ini, pos)));
            } catch (NumberFormatException ex) {
                throw new ExpresionInvalidaException("Número inválido: " + src.substring(ini, pos));
            }
        }
        if (Character.isLetter(c) || c == '_') {
            int ini = pos;
            while (pos < src.length() && (Character.isLetterOrDigit(src.charAt(pos)) || src.charAt(pos) == '_')) pos++;
            return new ExpresionVariable(src.substring(ini, pos));
        }
        throw new ExpresionInvalidaException("Carácter inesperado '" + c + "' en: " + src);
    }

    private void saltar() {
        while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
    }

    private boolean consumir(char ch) {
        if (pos < src.length() && src.charAt(pos) == ch) {
            pos++;
            return true;
        }
        return false;
    }
}
