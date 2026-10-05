package chain;

import bridge.Bloque;
import bridge.Documento;
import interpreter.Contexto;
import interpreter.ExpresionInvalidaException;
import interpreter.ParserExpresiones;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ConcreteHandler: reemplaza cada #{...} por su valor usando el Interpreter.
public class EvaluadorExpresiones extends ProcesadorHandler {
    private static final Pattern MARCADOR = Pattern.compile("#\\{([^}]*)\\}");
    private final Contexto contexto;

    public EvaluadorExpresiones(Contexto contexto) {
        this.contexto = contexto;
    }

    @Override
    protected boolean ejecutarProcesamiento(Documento doc) {
        System.out.println("  [Evaluador] Evaluando expresiones #{...}...");
        for (Bloque b : doc.getBloques()) {
            Matcher m = MARCADOR.matcher(b.getTexto());
            StringBuilder sb = new StringBuilder();
            while (m.find()) {
                String formula = m.group(1).trim();
                try {
                    String valor = ParserExpresiones.evaluarComoTexto(formula, contexto);
                    System.out.println("  [Evaluador] #{" + formula + "} = " + valor);
                    m.appendReplacement(sb, Matcher.quoteReplacement(valor));
                } catch (ExpresionInvalidaException e) {
                    System.out.println("  [Evaluador] ERROR CRÍTICO: " + e.getMessage() + " -> cadena interrumpida");
                    return false;
                }
            }
            m.appendTail(sb);
            b.setTexto(sb.toString());
        }
        return true;
    }
}
