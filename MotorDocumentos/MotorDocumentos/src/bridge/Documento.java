package bridge;

import flyweight.ElementoVisual;
import java.util.ArrayList;
import java.util.List;

// Abstraction del Bridge
public abstract class Documento {
    protected RenderEngine renderEngine;
    protected final List<ElementoVisual> elementos = new ArrayList<>();
    protected final List<Bloque> bloques = new ArrayList<>();

    public Documento(RenderEngine renderEngine) {
        this.renderEngine = renderEngine;
    }

    public void setRenderEngine(RenderEngine engine) {
        this.renderEngine = engine;
    }
    public RenderEngine getRenderEngine() {
        return renderEngine;
    }

    public void agregarElemento(ElementoVisual elemento) {
        elementos.add(elemento);
    }
    public void agregarBloque(Bloque bloque) {
        bloques.add(bloque);
    }

    public List<ElementoVisual> getElementos() {
        return elementos;
    }
    public List<Bloque> getBloques() {
        return bloques;
    }

    // Renderiza con el engine actual y devuelve el resultado exportado.
    public abstract String renderizar();

    protected void renderizarBloque(Bloque b) {
        switch (b.getTipo()) {
            case HEADER -> renderEngine.renderHeader(b.getTexto());
            case PARAGRAPH -> renderEngine.renderParagraph(b.getTexto());
            case TABLE -> renderEngine.renderTable(b.getTexto());
            case FOOTER -> renderEngine.renderFooter(b.getTexto());
        }
    }
}
