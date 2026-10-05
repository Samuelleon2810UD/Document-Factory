package bridge;
public interface RenderEngine {
    void reiniciar();
    void renderSaltoPagina(int numeroPagina);
    void renderHeader(String header);
    void renderParagraph(String p);
    void renderTable(String data);
    void renderFooter(String footer);
    String exportar();
    String extension();
}
