package com.document_factory.mediator;

/** MEDIATOR – Colega: botón que dispara la exportación. No sabe qué ocurre después. */
public class BotonExportar extends ComponenteUI {

    public void click() {
        System.out.println("[UI] BotonExportar → click");
        notificar("EXPORTAR");
    }
}
