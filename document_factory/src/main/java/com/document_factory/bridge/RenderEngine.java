package com.document_factory.bridge;

/**
 * BRIDGE – Implementor.
 * Define cómo se pinta cada tipo de bloque, sin saber nada de la jerarquía
 * de documentos (paginado / continuo). Ambas jerarquías varían de forma independiente.
 */
public interface RenderEngine {

    void renderHeader(String header);

    void renderParagraph(String p);

    /** Formato de data: filas separadas por ";" y celdas separadas por ",". */
    void renderTable(String data);

    void renderFooter(String footer);

    void exportar();

    /** Utilidad compartida por los motores para interpretar el formato de tabla. */
    static String[][] parseTabla(String data) {
        String[] filas = data.split(";");
        String[][] celdas = new String[filas.length][];
        for (int i = 0; i < filas.length; i++) {
            celdas[i] = filas[i].split(",");
        }
        return celdas;
    }
}
