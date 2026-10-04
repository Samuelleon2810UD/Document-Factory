package com.document_factory.bridge;

/** BRIDGE – Implementor concreto: genera HTML. */
public class HtmlRenderEngine implements RenderEngine {

    private final StringBuilder salida = new StringBuilder();

    private static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    @Override
    public void renderHeader(String header) {
        salida.append("<h1>").append(esc(header)).append("</h1>\n");
    }

    @Override
    public void renderParagraph(String p) {
        salida.append("<p>").append(esc(p)).append("</p>\n");
    }

    @Override
    public void renderTable(String data) {
        salida.append("<table>\n");
        for (String[] fila : RenderEngine.parseTabla(data)) {
            salida.append("  <tr>");
            for (String celda : fila) {
                salida.append("<td>").append(esc(celda)).append("</td>");
            }
            salida.append("</tr>\n");
        }
        salida.append("</table>\n");
    }

    @Override
    public void renderFooter(String footer) {
        salida.append("<footer>").append(esc(footer)).append("</footer>\n");
    }

    @Override
    public void exportar() {
        System.out.println("   ┌── Salida HTML ──");
        for (String linea : salida.toString().split("\n")) {
            System.out.println("   │ " + linea);
        }
        System.out.println("   └──");
        salida.setLength(0);
    }
}
