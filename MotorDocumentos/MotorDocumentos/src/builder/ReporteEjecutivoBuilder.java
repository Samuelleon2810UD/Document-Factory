package builder;

import bridge.DocumentoPaginado;
import bridge.RenderEngine;
import flyweight.FlyweightFactory;

// ConcreteBuilder: reporte paginado, encabezado grande con logo.
public class ReporteEjecutivoBuilder extends AbstractDocumentoBuilder {
    public ReporteEjecutivoBuilder(RenderEngine engine, FlyweightFactory fabrica) {
        super(new DocumentoPaginado(engine, 3), fabrica, "Serif", "#1F3864", "#000000", 2.0f, 1.0f);
    }

    @Override
    public DocumentoBuilder addHeader(String texto) {
        super.addHeader(texto);
        agregarIcono("logo-empresa");
        return this;
    }
}
