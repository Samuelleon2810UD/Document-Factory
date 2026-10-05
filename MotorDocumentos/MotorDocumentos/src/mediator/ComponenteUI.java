package mediator;

// Colleague: solo conoce al mediador, nunca a otros componentes.
public abstract class ComponenteUI {
    protected DocumentEditorMediator mediador;

    public void setMediador(DocumentEditorMediator mediador) {
        this.mediador = mediador;
    }
}
