package bridge;

// RefinedAbstraction: reparte los bloques en páginas.
public class DocumentoPaginado extends Documento {
    private int numPaginas = 1;
    private final int bloquesPorPagina;

    public DocumentoPaginado(RenderEngine engine) {
        this(engine, 3);
    }

    public DocumentoPaginado(RenderEngine engine, int bloquesPorPagina) {
        super(engine);
        this.bloquesPorPagina = bloquesPorPagina;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    @Override
    public String renderizar() {
        renderEngine.reiniciar();
        int pagina = 1;
        int enPagina = 0;
        renderEngine.renderSaltoPagina(pagina);
        for (Bloque b : bloques) {
            if (enPagina == bloquesPorPagina) {
                pagina++;
                renderEngine.renderSaltoPagina(pagina);
                enPagina = 0;
            }
            renderizarBloque(b);
            enPagina++;
        }
        numPaginas = pagina;
        return renderEngine.exportar();
    }
}
