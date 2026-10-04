package com.document_factory.mediator;

/**
 * MEDIATOR – Interfaz del mediador.
 * Los componentes de la UI solo conocen esta interfaz; nunca se hablan entre sí.
 */
public interface DocumentEditorMediator {
    void notificar(ComponenteUI origen, String evento);
}
