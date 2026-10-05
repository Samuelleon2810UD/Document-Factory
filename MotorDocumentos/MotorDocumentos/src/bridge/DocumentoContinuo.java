package bridge;

// RefinedAbstraction: flujo continuo sin saltos de página
public class DocumentoContinuo extends Documento {
    private double longitudTotal;

    public DocumentoContinuo(RenderEngine engine) {
        super(engine);
    }

    public double getLongitudTotal() {
        return longitudTotal;
    }

    @Override
    public String renderizar() {
        renderEngine.reiniciar();
        longitudTotal = 0;
        for (Bloque b : bloques) {
            renderizarBloque(b);
            longitudTotal += (b.getTexto().split("\n").length) * 14.0;
        }
        return renderEngine.exportar();
    }
}
