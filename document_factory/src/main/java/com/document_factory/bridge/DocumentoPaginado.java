package com.document_factory.bridge;

/** BRIDGE – Abstracción refinada: documento dividido en páginas (ej. reporte). */
public class DocumentoPaginado extends Documento {

    private static final int ELEMENTOS_POR_PAGINA = 60;
    private int numPaginas;

    public DocumentoPaginado(RenderEngine renderEngine) {
        super(renderEngine);
    }

    @Override
    public void renderizar() {
        numPaginas = Math.max(1, (int) Math.ceil(elementos.size() / (double) ELEMENTOS_POR_PAGINA));
        System.out.println("   Documento PAGINADO → " + numPaginas + " página(s), motor: "
                + renderEngine.getClass().getSimpleName());
        renderizarSecciones();
        dibujarElementos();
    }
}
