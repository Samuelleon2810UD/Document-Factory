package chain;

import bridge.Bloque;
import bridge.Documento;

// ConcreteHandler: detecta marcadores #{...} y etiquetas < > corruptos.
public class ValidadorSintaxis extends ProcesadorHandler {
    @Override
    protected boolean ejecutarProcesamiento(Documento doc) {
        System.out.println("  [Validador] Verificando sintaxis...");
        for (Bloque b : doc.getBloques()) {
            String error = validar(b.getTexto());
            if (error != null) {
                System.out.println("  [Validador] ERROR CRÍTICO (" + b.getTipo() + "): " + error + " -> cadena interrumpida");
                return false;
            }
        }
        System.out.println("  [Validador] OK");
        return true;
    }

    private String validar(String t) {
        int i = 0;
        while ((i = t.indexOf("#{", i)) >= 0) {
            int fin = t.indexOf('}', i + 2);
            if (fin < 0) return "marcador '#{' sin cerrar en \"" + t + "\"";
            int otro = t.indexOf("#{", i + 2);
            if (otro >= 0 && otro < fin) return "marcadores anidados en \"" + t + "\"";
            if (t.substring(i + 2, fin).isBlank()) return "marcador vacío en \"" + t + "\"";
            i = fin + 1;
        }
        if (t.chars().filter(c -> c == '<').count() != t.chars().filter(c -> c == '>').count()) {
            return "etiquetas '<' '>' desbalanceadas en \"" + t + "\"";
        }
        return null;
    }
}
