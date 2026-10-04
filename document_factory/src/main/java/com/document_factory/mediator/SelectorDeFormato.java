package com.document_factory.mediator;

/** MEDIATOR – Colega: elige el formato de salida (PDF, HTML, MARKDOWN). */
public class SelectorDeFormato extends ComponenteUI {

    private String formatoSeleccionado;

    public void seleccionarFormato(String formato) {
        this.formatoSeleccionado = formato;
        System.out.println("[UI] SelectorDeFormato → " + formato);
        notificar("FORMATO_CAMBIADO");
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }
}
