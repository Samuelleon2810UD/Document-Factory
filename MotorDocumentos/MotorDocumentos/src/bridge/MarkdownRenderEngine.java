package bridge;

// ConcreteImplementor: genera Markdown.
public class MarkdownRenderEngine implements RenderEngine {
    private final StringBuilder sb = new StringBuilder();

    @Override
    public void reiniciar() {
        sb.setLength(0);
    }

    @Override
    public void renderSaltoPagina(int n) {
        if (n > 1) sb.append("\n---\n\n");
    }

    @Override
    public void renderHeader(String h) { sb.append("# ").append(h).append("\n\n"); }

    @Override
    public void renderParagraph(String p) { sb.append(p).append("\n\n"); }

    @Override
    public void renderTable(String data) {
        String[] filas = data.split("\n");
        for (int i = 0; i < filas.length; i++) {
            String[] celdas = filas[i].split("\\|", -1);
            sb.append("| ").append(String.join(" | ", celdas)).append(" |\n");
            if (i == 0) {
                sb.append("|").append(" --- |".repeat(celdas.length)).append("\n");
            }
        }
        sb.append("\n");
    }

    @Override
    public void renderFooter(String f) {
        sb.append("*").append(f).append("*\n");
    }

    @Override
    public String exportar() {
        return sb.toString();
    }

    @Override
    public String extension() {
        return "md";
    }
}
