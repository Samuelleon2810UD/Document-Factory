package com.document_factory.mediator;

/** MEDIATOR – Colega abstracto: conoce al mediador y le avisa de sus eventos. */
public abstract class ComponenteUI {

    protected DocumentEditorMediator mediador;

    public void setMediador(DocumentEditorMediator mediador) {
        this.mediador = mediador;
    }

    protected void notificar(String evento) {
        if (mediador != null) {
            mediador.notificar(this, evento);
        }
    }
}
