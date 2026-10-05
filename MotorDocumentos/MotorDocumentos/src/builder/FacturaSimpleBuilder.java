package builder;

import bridge.DocumentoContinuo;
import bridge.RenderEngine;
import flyweight.FlyweightFactory;

// ConcreteBuilder: factura continua, pie con icono de pago.
public class FacturaSimpleBuilder extends AbstractDocumentoBuilder {
    public FacturaSimpleBuilder(RenderEngine engine, FlyweightFactory fabrica) {
        super(new DocumentoContinuo(engine), fabrica, "Monospace", "#7B1F1F", "#222222", 1.5f, 1.0f);
    }

    @Override
    public DocumentoBuilder addFooter(String texto) {
        super.addFooter(texto);
        agregarIcono("check-pago");
        return this;
    }
}
