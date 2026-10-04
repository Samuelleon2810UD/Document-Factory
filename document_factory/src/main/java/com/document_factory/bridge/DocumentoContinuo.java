package com.document_factory.bridge;

import com.document_factory.flyweight.ElementoVisual;

/** BRIDGE – Abstracción refinada: documento de una sola tira sin saltos de página (ej. factura). */
public class DocumentoContinuo extends Documento {

    private double longitudTotal;

    public DocumentoContinuo(RenderEngine renderEngine) {
        super(renderEngine);
    }

    @Override
    public void renderizar() {
        int maxY = 0;
        for (ElementoVisual e : elementos) {
            maxY = Math.max(maxY, e.getPosY());
        }
        longitudTotal = maxY + 20.0;
        System.out.println("   Documento CONTINUO → longitud " + longitudTotal + " mm, motor: "
                + renderEngine.getClass().getSimpleName());
        renderizarSecciones();
        dibujarElementos();
    }
}
