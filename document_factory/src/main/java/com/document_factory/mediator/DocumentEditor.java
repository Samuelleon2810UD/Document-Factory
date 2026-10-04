package com.document_factory.mediator;
import com.document_factory.bridge.Documento;
import com.document_factory.bridge.HtmlRenderEngine;
import com.document_factory.bridge.MarkdownRenderEngine;
import com.document_factory.bridge.PdfRenderEngine;
import com.document_factory.bridge.RenderEngine;
import com.document_factory.builder.DocumentoBuilder;
import com.document_factory.chain.ProcesadorHandler;

/**
 * MEDIATOR concreto – el director de orquesta de los 6 patrones.
 *
 * Los componentes de la UI (SelectorDeFormato, BarraDeHerramientas, BotonExportar,
 * VistaPrevia) solo hablan con este objeto, y él coordina el resto:
 *
 *   BotonExportar ──(Mediator)──► DocumentEditor
 *        1. BUILDER   → obtiene el Documento construido (con ElementoVisual FLYWEIGHT)
 *        2. CHAIN     → pasa el documento por la tubería de validación
 *                       └ (último eslabón) INTERPRETER evalúa ${expresiones}
 *        3. BRIDGE    → asigna el RenderEngine elegido y renderiza
 *        4. MEDIATOR  → actualiza la VistaPrevia
 */
public class DocumentEditor implements DocumentEditorMediator {

    private final SelectorDeFormato selectorFormato = new SelectorDeFormato();
    private final BarraDeHerramientasBuilder barraHerramientas = new BarraDeHerramientasBuilder();
    private final VistaPrevia vistaPrevia = new VistaPrevia();
    private final BotonExportar botonExportar = new BotonExportar();

    private DocumentoBuilder builderActual;
    private Documento documentoActual;
    private final ProcesadorHandler tuberia;     // primer eslabón de la cadena
    private RenderEngine motorActual;            // formato elegido (BRIDGE)
    private boolean documentoProcesado = false;

    public DocumentEditor(ProcesadorHandler tuberia) {
        this.tuberia = tuberia;
        // MEDIATOR: cada colega se registra con el mediador.
        selectorFormato.setMediador(this);
        barraHerramientas.setMediador(this);
        vistaPrevia.setMediador(this);
        botonExportar.setMediador(this);
    }

    /** Cambia el builder activo (reporte, factura, ...) y reinicia el estado del documento. */
    public void setBuilder(DocumentoBuilder builder) {
        this.builderActual = builder;
        this.documentoActual = null;
        this.documentoProcesado = false;
    }

    // ---------------------------------------------------------------- MEDIATOR

    @Override
    public void notificar(ComponenteUI origen, String evento) {
        if (origen == selectorFormato && evento.equals("FORMATO_CAMBIADO")) {
            cambiarFormato(selectorFormato.getFormatoSeleccionado());
        } else if (origen == botonExportar && evento.equals("EXPORTAR")) {
            exportarDocumento();
        } else if (origen == barraHerramientas && evento.startsWith("AGREGAR_SECCION:")) {
            agregarSeccionPorDefecto(evento.substring("AGREGAR_SECCION:".length()));
        }
    }

    // ---------------------------------------------------------------- BRIDGE

    public void cambiarFormato(String formato) {
        RenderEngine motor;
        switch (formato.toUpperCase()) {
            case "PDF":      motor = new PdfRenderEngine();      break;
            case "HTML":     motor = new HtmlRenderEngine();     break;
            case "MARKDOWN":
            case "MD":       motor = new MarkdownRenderEngine(); break;
            default:
                System.out.println("   ✖ [DocumentEditor] Formato no soportado: " + formato);
                return;
        }
        this.motorActual = motor;
        // Si ya existe un documento, el BRIDGE permite cambiarle el motor sin reconstruirlo.
        if (documentoActual != null) {
            documentoActual.setRenderEngine(motor);
        }
        System.out.println("   → [DocumentEditor] formato activo: " + formato.toUpperCase());
    }

    // ---------------------------------------------------------------- BUILDER

    private void agregarSeccionPorDefecto(String tipo) {
        if (builderActual == null) {
            System.out.println("   ✖ [DocumentEditor] No hay builder activo");
            return;
        }
        switch (tipo.toUpperCase()) {
            case "HEADER":    builderActual.addHeader("Encabezado nuevo");                    break;
            case "PARAGRAPH": builderActual.addParagraph("Párrafo nuevo");                    break;
            case "TABLE":     builderActual.addTable(2, 2);                                   break;
            case "FOOTER":    builderActual.addFooter("Generado por el Motor de Documentos"); break;
            default:
                System.out.println("   ✖ [DocumentEditor] Sección desconocida: " + tipo);
                return;
        }
        documentoProcesado = false; // el contenido cambió: hay que volver a pasar por la cadena
    }

    // ---------------------------------------------------------------- FLUJO COMPLETO

    public void exportarDocumento() {
        if (builderActual == null) {
            System.out.println("   ✖ [DocumentEditor] No hay documento que exportar");
            return;
        }
        // 1. BUILDER: documento listo (sus ElementoVisual ya comparten flyweights)
        documentoActual = builderActual.build();

        // 2. CHAIN (+ INTERPRETER dentro del último eslabón)
        if (!documentoProcesado) {
            System.out.println("   Procesando documento en la tubería...");
            if (tuberia != null && !tuberia.procesar(documentoActual)) {
                System.out.println("   ⛔ Exportación cancelada: el documento no superó la tubería");
                return;
            }
            documentoProcesado = true;
        }

        // 3. BRIDGE: se aplica el motor elegido en la UI y se renderiza
        if (motorActual != null) {
            documentoActual.setRenderEngine(motorActual);
        }
        documentoActual.renderizar();

        // 4. MEDIATOR: se refresca la vista previa y se exporta
        vistaPrevia.actualizar(documentoActual);
        documentoActual.getRenderEngine().exportar();
    }

    // Accesos para que el Main simule las interacciones del usuario con la UI.
    public SelectorDeFormato getSelectorFormato() { return selectorFormato; }
    public BarraDeHerramientasBuilder getBarraHerramientas() { return barraHerramientas; }
    public BotonExportar getBotonExportar() { return botonExportar; }
}
