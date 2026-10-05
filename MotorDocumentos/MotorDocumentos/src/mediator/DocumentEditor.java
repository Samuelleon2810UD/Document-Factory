package mediator;

import bridge.Documento;
import bridge.HtmlRenderEngine;
import bridge.MarkdownRenderEngine;
import bridge.PdfRenderEngine;
import bridge.RenderEngine;
import builder.DocumentoBuilder;
import builder.FacturaSimpleBuilder;
import builder.ReporteEjecutivoBuilder;
import chain.ProcesadorHandler;
import flyweight.FlyweightFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * ConcreteMediator. Coordina los componentes y reconfigura Builder y RenderEngine cada vez que cambia el formato, la plantilla o las secciones.
 */
public class DocumentEditor implements DocumentEditorMediator {
    private final SelectorDeFormato selectorFormato;
    private final BarraDeHerramientasBuilder barraHerramientas;
    private final VistaPrevia vistaPrevia;
    private final BotonExportar botonExportar;
    private DocumentoBuilder builderActual;
    private Documento documentoActual;

    private final FlyweightFactory fabrica;
    private final ProcesadorHandler cadena;
    private final Path dirSalida;
    private String formatoActual;
    private String plantilla = "REPORTE";
    private final List<String[]> secciones = new ArrayList<>();

    public DocumentEditor(SelectorDeFormato selector, BarraDeHerramientasBuilder barra, VistaPrevia vista, BotonExportar boton, FlyweightFactory fabrica, ProcesadorHandler cadena, Path dirSalida) {
        this.selectorFormato = selector;
        this.barraHerramientas = barra;
        this.vistaPrevia = vista;
        this.botonExportar = boton;
        this.fabrica = fabrica;
        this.cadena = cadena;
        this.dirSalida = dirSalida;
        this.formatoActual = selector.getFormatoSeleccionado();
        for (ComponenteUI c : List.of(selector, barra, vista, boton)) {
            c.setMediador(this);
        }
    }

    @Override
    public void notificar(ComponenteUI origen, String evento) {
        System.out.println("[Mediator] " + origen.getClass().getSimpleName() + " -> " + evento);
        switch (evento) {
            case "FORMATO_CAMBIADO" -> cambiarFormato(selectorFormato.getFormatoSeleccionado());
            case "PLANTILLA_CAMBIADA" -> {
                plantilla = barraHerramientas.getPlantilla();
                reconstruir();
            }
            case "SECCION_AGREGADA" -> {
                secciones.add(new String[]{barraHerramientas.getUltimoTipo(), barraHerramientas.getUltimoContenido()});
                reconstruir();
            }
            case "EXPORTAR" -> exportarDocumento();
            default -> System.out.println("[Mediator] Evento desconocido: " + evento);
        }
    }

    public void cambiarFormato(String formato) {
        this.formatoActual = formato;
        reconstruir();
    }

    public void exportarDocumento() {
        if (documentoActual == null || secciones.isEmpty()) {
            System.out.println("[Editor] No hay contenido para exportar.");
            return;
        }
        System.out.println("[Editor] Procesando documento por la cadena de responsabilidad...");
        if (!cadena.procesar(documentoActual)) {
            System.out.println("[Editor] Exportación CANCELADA: la cadena fue interrumpida.");
            return;
        }
        String resultado = documentoActual.renderizar();
        Path destino = dirSalida.resolve("documento_" + formatoActual.toLowerCase() + "."
                + documentoActual.getRenderEngine().extension());
        try {
            Files.createDirectories(dirSalida);
            Files.writeString(destino, resultado);
        } catch (IOException e) {
            System.out.println("[Editor] No se pudo escribir el archivo: " + e.getMessage());
        }
        System.out.println("[Editor] Exportado (" + formatoActual + ") -> " + destino);
        System.out.println("contenido");
        System.out.print(resultado);
    }

    public Documento getDocumentoActual() { return documentoActual; }

    /** Recrea Builder y RenderEngine según la configuración actual y repite las secciones. */
    private void reconstruir() {
        RenderEngine engine = crearEngine(formatoActual);
        builderActual = "FACTURA".equalsIgnoreCase(plantilla)
                ? new FacturaSimpleBuilder(engine, fabrica)
                : new ReporteEjecutivoBuilder(engine, fabrica);
        for (String[] s : secciones) {
            aplicarSeccion(builderActual, s[0], s[1]);
        }
        documentoActual = builderActual.build();
        vistaPrevia.actualizar(documentoActual);
    }

    private RenderEngine crearEngine(String formato) {
        return switch (formato.toUpperCase()) {
            case "PDF" -> new PdfRenderEngine();
            case "HTML" -> new HtmlRenderEngine();
            case "MARKDOWN", "MD" -> new MarkdownRenderEngine();
            default -> throw new IllegalArgumentException("Formato no soportado: " + formato);
        };
    }

    private void aplicarSeccion(DocumentoBuilder b, String tipo, String contenido) {
        switch (tipo.toUpperCase()) {
            case "HEADER" -> b.addHeader(contenido);
            case "PARAGRAPH" -> b.addParagraph(contenido);
            case "FOOTER" -> b.addFooter(contenido);
            case "TABLE" -> {
                if (contenido.matches("\\d+x\\d+")) {
                    String[] d = contenido.split("x");
                    b.addTable(Integer.parseInt(d[0]), Integer.parseInt(d[1]));
                } else {
                    String[] filas = contenido.split(";");
                    String[][] datos = new String[filas.length][];
                    for (int i = 0; i < filas.length; i++) {
                        String[] celdas = filas[i].split("\\|", -1);
                        for (int j = 0; j < celdas.length; j++) celdas[j] = celdas[j].trim();
                        datos[i] = celdas;
                    }
                    b.addTable(datos);
                }
            }
            default -> throw new IllegalArgumentException("Tipo de sección no soportado: " + tipo);
        }
    }
}
