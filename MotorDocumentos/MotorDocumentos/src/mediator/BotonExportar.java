package mediator;

public class BotonExportar extends ComponenteUI {
    public void click() {
        System.out.println("[UI] BotonExportar: click");
        mediador.notificar(this, "EXPORTAR");
    }
}
