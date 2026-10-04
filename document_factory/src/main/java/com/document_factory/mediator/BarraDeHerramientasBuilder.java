package com.document_factory.mediator;

/**
 * MEDIATOR – Colega: barra de herramientas para añadir secciones.
 * Aunque el diagrama la llama "...Builder", su rol aquí es el de un componente de UI
 * (Colleague); el patrón Builder real está en el paquete builder.
 */
public class BarraDeHerramientasBuilder extends ComponenteUI {

    public void agregarSeccion(String tipo) {
        System.out.println("[UI] BarraDeHerramientas → agregar sección " + tipo);
        notificar("AGREGAR_SECCION:" + tipo);
    }
}
