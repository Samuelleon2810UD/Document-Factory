package com.document_factory.chain;

import java.util.Arrays;
import java.util.List;
import com.document_factory.bridge.Documento;

/** CHAIN – Eslabón 2: rechaza documentos que contengan palabras de la lista negra. */
public class FiltroPalabrasProhibidas extends ProcesadorHandler {

    private final List<String> palabrasProhibidas;

    public FiltroPalabrasProhibidas(String... palabras) {
        this.palabrasProhibidas = Arrays.asList(palabras);
    }

    @Override
    protected boolean ejecutarProcesamiento(Documento doc) {
        for (Documento.Seccion s : doc.getSecciones()) {
            String minusculas = s.getTexto().toLowerCase();
            for (String prohibida : palabrasProhibidas) {
                if (minusculas.contains(prohibida.toLowerCase())) {
                    return rechazar("Palabra prohibida '" + prohibida + "' en: \"" + s.getTexto() + "\"");
                }
            }
        }
        return aprobar("Sin palabras prohibidas");
    }
}
