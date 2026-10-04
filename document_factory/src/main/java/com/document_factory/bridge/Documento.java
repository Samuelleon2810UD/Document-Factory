package com.document_factory.bridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.document_factory.flyweight.ElementoVisual;

/**
 * BRIDGE – Abstracción.
 * Mantiene una referencia (el "puente") a un {@link RenderEngine}; las subclases
 * (paginado / continuo) refinan la abstracción sin conocer el formato de salida.
 *
 * COMBINACIÓN DE PATRONES: este es el producto que
 *  - los BUILDERS construyen,
 *  - los ElementoVisual (FLYWEIGHT) pueblan,
 *  - la cadena CHAIN OF RESPONSIBILITY valida/transforma, y
 *  - el MEDIATOR (DocumentEditor) manda a renderizar cambiando el motor en caliente.
 */
public abstract class Documento {

    public enum TipoSeccion { HEADER, PARAGRAPH, TABLE, FOOTER }

    /**
     * Bloque lógico del documento. (Pequeña extensión sobre el diagrama: el contenido
     * textual debe conservarse para poder validarlo, evaluarlo y renderizarlo.)
     */
    public static final class Seccion {
        private final TipoSeccion tipo;
        private String texto;
        private List<ElementoVisual> elementos;

        Seccion(TipoSeccion tipo, String texto, List<ElementoVisual> elementos) {
            this.tipo = tipo;
            this.texto = texto;
            this.elementos = elementos;
        }

        public TipoSeccion getTipo() { return tipo; }
        public String getTexto() { return texto; }
        public List<ElementoVisual> getElementos() { return elementos; }
    }

    // El puente: la abstracción delega todo el dibujado en este objeto.
    protected RenderEngine renderEngine;
    protected List<ElementoVisual> elementos = new ArrayList<>();
    private final List<Seccion> secciones = new ArrayList<>();

    public Documento(RenderEngine renderEngine) {
        this.renderEngine = renderEngine;
    }

    /** BRIDGE: permite cambiar el formato de salida en tiempo de ejecución. */
    public void setRenderEngine(RenderEngine engine) {
        this.renderEngine = engine;
    }

    public RenderEngine getRenderEngine() {
        return renderEngine;
    }

    public void agregarElemento(ElementoVisual elemento) {
        elementos.add(elemento);
    }

    public void agregarSeccion(TipoSeccion tipo, String texto, List<ElementoVisual> elementosSeccion) {
        secciones.add(new Seccion(tipo, texto, elementosSeccion));
        elementos.addAll(elementosSeccion);
    }

    /**
     * Usado por la cadena (EvaluadorExpresiones) tras interpretar las expresiones:
     * cambia el texto y reemplaza los ElementoVisual de esa sección.
     */
    public void reemplazarTexto(Seccion seccion, String nuevoTexto, List<ElementoVisual> nuevos) {
        elementos.removeAll(seccion.elementos);
        seccion.texto = nuevoTexto;
        seccion.elementos = nuevos;
        elementos.addAll(nuevos);
    }

    public List<Seccion> getSecciones() {
        return Collections.unmodifiableList(secciones);
    }

    public List<ElementoVisual> getElementos() {
        return Collections.unmodifiableList(elementos);
    }

    /** Pasos comunes a las refinadas: delegar cada bloque al motor (BRIDGE). */
    protected void renderizarSecciones() {
        for (Seccion s : secciones) {
            switch (s.tipo) {
                case HEADER:    renderEngine.renderHeader(s.texto);    break;
                case PARAGRAPH: renderEngine.renderParagraph(s.texto); break;
                case TABLE:     renderEngine.renderTable(s.texto);     break;
                case FOOTER:    renderEngine.renderFooter(s.texto);    break;
            }
        }
    }

    /** FLYWEIGHT: cada ElementoVisual dibuja delegando en su glifo compartido. */
    protected void dibujarElementos() {
        for (ElementoVisual e : elementos) {
            e.dibujar();
        }
    }

    public abstract void renderizar();
}
