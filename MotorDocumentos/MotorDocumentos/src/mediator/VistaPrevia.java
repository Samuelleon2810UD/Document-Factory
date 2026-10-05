package mediator;

import bridge.Documento;

public class VistaPrevia extends ComponenteUI {
    public void actualizar(Documento doc) {
        System.out.println("[UI] VistaPrevia: " + doc.getClass().getSimpleName() + " con " + doc.getBloques().size() + " bloques, " + doc.getElementos().size() + " elementos visuales, motor " + doc.getRenderEngine().getClass().getSimpleName());
    }
}
