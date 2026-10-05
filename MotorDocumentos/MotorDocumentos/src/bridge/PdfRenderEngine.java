package bridge;

// ConcreteImplementor: genera un PDF simulado (texto con operadores tipo PDF).
public class PdfRenderEngine implements RenderEngine {
    private final StringBuilder sb = new StringBuilder();

    @Override public void reiniciar() {
        sb.setLength(0);
    }

    @Override public void renderSaltoPagina(int n) {
        sb.append("Página").append(n).append("\n");
    }

    @Override public void renderHeader(String h) {
        sb.append("BT /F1 24 Tf (").append(h).append(") Tj ET\n");
    }

    @Override public void renderParagraph(String p) {
        sb.append("BT /F1 12 Tf (").append(p).append(") Tj ET\n");
    }

    @Override public void renderTable(String data) {
        for (String fila : data.split("\n")) {
            sb.append("BT /F2 10 Tf (").append(fila.replace("|", "   |   ")).append(") Tj ET\n");
        }
    }

    @Override public void renderFooter(String f) {
        sb.append("BT /F1 8 Tf (").append(f).append(") Tj ET\n");
    }

    @Override public String exportar() { return "%PDF-1.4 (simulado)\n" + sb + "%%EOF\n"; }

    @Override public String extension() { return "pdf.txt"; }
}
