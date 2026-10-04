package com.document_factory.bridge;

/** BRIDGE – Implementor concreto: genera Markdown. */
public class MarkdownRenderEngine implements RenderEngine {

    private final StringBuilder salida = new StringBuilder();

    @Override
    public void renderHeader(String header) {
        salida.append("# ").append(header).append("\n\n");
    }

    @Override
    public void renderParagraph(String p) {
        salida.append(p).append("\n\n");
    }

    @Override
    public void renderTable(String data) {
        String[][] filas = RenderEngine.parseTabla(data);
        for (int i = 0; i < filas.length; i++) {
            salida.append("| ").append(String.join(" | ", filas[i])).append(" |\n");
            if (i == 0) {
                salida.append("|");
                for (int j = 0; j < filas[i].length; j++) {
                    salida.append(" --- |");
                }
                salida.append("\n");
            }
        }
        salida.append("\n");
    }

    @Override
    public void renderFooter(String footer) {
        salida.append("---\n*").append(footer).append("*\n");
    }

    @Override
    public void exportar() {
        System.out.println("   ┌── Salida Markdown ──");
        for (String linea : salida.toString().split("\n")) {
            System.out.println("   │ " + linea);
        }
        System.out.println("   └──");
        salida.setLength(0);
    }
}
