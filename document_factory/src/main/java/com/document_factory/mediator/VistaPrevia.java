package com.document_factory.mediator;

import com.document_factory.bridge.Documento;

/** MEDIATOR – Colega: muestra un resumen del documento; el mediador la actualiza. */
public class VistaPrevia extends ComponenteUI {

    public void actualizar(Documento doc) {
        System.out.println("[UI] VistaPrevia → " + doc.getSecciones().size() + " sección(es), "
                + doc.getElementos().size() + " elemento(s) visual(es), motor "
                + doc.getRenderEngine().getClass().getSimpleName());
    }
}
