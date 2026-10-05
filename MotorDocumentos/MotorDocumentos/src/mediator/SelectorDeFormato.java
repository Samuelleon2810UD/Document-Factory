package mediator;

public class SelectorDeFormato extends ComponenteUI {
    private String formatoSeleccionado = "PDF";

    public void seleccionarFormato(String formato) {
        this.formatoSeleccionado = formato;
        System.out.println("[UI] SelectorDeFormato: usuario eligió " + formato);
        mediador.notificar(this, "FORMATO_CAMBIADO");
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }
}
