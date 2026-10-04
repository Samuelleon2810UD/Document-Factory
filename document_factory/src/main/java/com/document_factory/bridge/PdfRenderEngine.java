package com.document_factory.bridge;

/** BRIDGE – Implementor concreto: simula operadores de contenido PDF. */
public class PdfRenderEngine implements RenderEngine {

    private final StringBuilder salida = new StringBuilder();

    @Override
    public void renderHeader(String header) {
        salida.append("BT /F1 24 Tf (").append(header).append(") Tj ET\n");
    }

    @Override
    public void renderParagraph(String p) {
        salida.append("BT /F1 12 Tf (").append(p).append(") Tj ET\n");
    }

    @Override
    public void renderTable(String data) {
        for (String[] fila : RenderEngine.parseTabla(data)) {
            salida.append("BT /F1 10 Tf ");
            for (String celda : fila) {
                salida.append("[").append(celda).append("] ");
            }
            salida.append("ET\n");
        }
    }

    @Override
    public void renderFooter(String footer) {
        salida.append("BT /F1 8 Tf (").append(footer).append(") Tj ET\n");
    }

    @Override
    public void exportar() {
        System.out.println("   ┌── Salida PDF (simulada) ──");
        for (String linea : salida.toString().split("\n")) {
            System.out.println("   │ " + linea);
        }
        System.out.println("   └── %%EOF");
        salida.setLength(0);
    }
}
