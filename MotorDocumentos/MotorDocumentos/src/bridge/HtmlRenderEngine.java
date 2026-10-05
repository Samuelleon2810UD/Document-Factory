package bridge;

// ConcreteImplementor: genera HTML.
public class HtmlRenderEngine implements RenderEngine {
    private final StringBuilder sb = new StringBuilder();

    private static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    @Override
    public void reiniciar() {
        sb.setLength(0);
    }

    @Override
    public void renderSaltoPagina(int n) {
        if (n > 1) sb.append("<hr class=\"page-break\">\n");
    }

    @Override
    public void renderHeader(String h) {
        sb.append("<h1>").append(esc(h)).append("</h1>\n");
    }

    @Override
    public void renderParagraph(String p) {
        sb.append("<p>").append(esc(p)).append("</p>\n");
    }

    @Override
    public void renderTable(String data) {
        sb.append("<table border=\"1\">\n");
        String[] filas = data.split("\n");
        for (int i = 0; i < filas.length; i++) {
            String tag = (i == 0) ? "th" : "td";
            sb.append("  <tr>");
            for (String celda : filas[i].split("\\|", -1)) {
                sb.append("<").append(tag).append(">").append(esc(celda)).append("</").append(tag).append(">");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
    }

    @Override
    public void renderFooter(String f) {
        sb.append("<footer>").append(esc(f)).append("</footer>\n");
    }

    @Override
    public String exportar() {
        return "<!DOCTYPE html>\n<html>\n<head><meta charset=\"UTF-8\"><title>Documento</title></head>\n<body>\n" + sb + "</body>\n</html>\n";
    }

    @Override
    public String extension() {
        return "html";
    }
}
