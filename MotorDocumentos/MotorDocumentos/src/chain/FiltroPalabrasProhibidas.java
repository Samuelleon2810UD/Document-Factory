package chain;

import bridge.Bloque;
import bridge.Documento;
import java.util.List;
import java.util.regex.Pattern;

// ConcreteHandler / Sanitizador: oculta palabras no permitidas con "***".
public class FiltroPalabrasProhibidas extends ProcesadorHandler {
    private final List<String> palabrasProhibidas;

    public FiltroPalabrasProhibidas(List<String> palabrasProhibidas) {
        this.palabrasProhibidas = palabrasProhibidas;
    }

    @Override
    protected boolean ejecutarProcesamiento(Documento doc) {
        System.out.println("  [Filtro] Sanitizando palabras prohibidas " + palabrasProhibidas + "...");
        for (Bloque b : doc.getBloques()) {
            String original = b.getTexto();
            String texto = original;
            for (String p : palabrasProhibidas) {
                texto = Pattern.compile("(?iu)\\b" + Pattern.quote(p) + "\\b").matcher(texto).replaceAll("***");
            }
            if (!texto.equals(original)) {
                System.out.println("  [Filtro] Texto sanitizado: \"" + original + "\" -> \"" + texto + "\"");
                b.setTexto(texto);
            }
        }
        return true;
    }
}
