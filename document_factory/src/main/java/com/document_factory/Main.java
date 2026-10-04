package com.document_factory;

import com.document_factory.bridge.PdfRenderEngine;
import com.document_factory.builder.FacturaSimpleBuilder;
import com.document_factory.builder.ReporteEjecutivoBuilder;
import com.document_factory.chain.EvaluadorExpresiones;
import com.document_factory.chain.FiltroPalabrasProhibidas;
import com.document_factory.chain.ProcesadorHandler;
import com.document_factory.chain.ValidadorSintaxis;
import com.document_factory.flyweight.ElementoVisual;
import com.document_factory.flyweight.FlyweightFactory;
import com.document_factory.interpreter.Contexto;
import com.document_factory.mediator.DocumentEditor;

/**
 * Demo del Motor de Documentos Inteligentes.
 *
 * Ensambla los 6 patrones:
 *   FLYWEIGHT (fábrica compartida) → INTERPRETER (contexto de variables) →
 *   CHAIN (tubería) → MEDIATOR (DocumentEditor) → BUILDER → BRIDGE (formatos).
 */
public class Main {

    public static void main(String[] args) {

        // ---- FLYWEIGHT: una única fábrica para TODO el sistema -------------
        FlyweightFactory fabrica = new FlyweightFactory();

        // ---- INTERPRETER: variables disponibles dentro de ${...} -----------
        Contexto contexto = new Contexto();
        contexto.asignarVariable("precio", 250.0);
        contexto.asignarVariable("unidades", 4.0);
        contexto.asignarVariable("envio", 15.0);

        // ---- CHAIN: Validador → Filtro → Evaluador (usa el Interpreter) ----
        ProcesadorHandler validador = new ValidadorSintaxis();
        validador.setSiguiente(new FiltroPalabrasProhibidas("spam", "fraude", "secreto"))
                 .setSiguiente(new EvaluadorExpresiones(contexto, fabrica));

        // ---- MEDIATOR: el editor recibe la tubería y coordina la UI --------
        DocumentEditor editor = new DocumentEditor(validador);

        // ======================= ESCENARIO 1: Reporte (paginado) en 3 formatos =======================
        titulo("ESCENARIO 1 – Reporte ejecutivo (DocumentoPaginado) en PDF, HTML y Markdown");
        // BUILDER: el motor inicial es irrelevante; el Mediator lo reemplaza al exportar (BRIDGE).
        ReporteEjecutivoBuilder reporte = new ReporteEjecutivoBuilder(new PdfRenderEngine(), fabrica);
        editor.setBuilder(reporte);
        reporte.addHeader("Reporte Ejecutivo Q3")
               .addParagraph("Ventas totales: ${(precio + 10) * unidades} COP")
               .addTable(2, 3);
        editor.getBarraHerramientas().agregarSeccion("FOOTER"); // MEDIATOR → BUILDER

        for (String formato : new String[]{"PDF", "HTML", "MARKDOWN"}) {
            editor.getSelectorFormato().seleccionarFormato(formato); // MEDIATOR → BRIDGE
            editor.getBotonExportar().click();                       // MEDIATOR → CHAIN → BRIDGE
            System.out.println();
        }

        // ======================= ESCENARIO 2: Factura (continua) =======================
        titulo("ESCENARIO 2 – Factura simple (DocumentoContinuo) en Markdown");
        FacturaSimpleBuilder factura = new FacturaSimpleBuilder(new PdfRenderEngine(), fabrica);
        editor.setBuilder(factura);
        factura.addHeader("Factura #0042")
               .addParagraph("Subtotal: ${precio * unidades}")
               .addParagraph("Total con envío: ${precio * unidades + envio}")
               .addFooter("Gracias por su compra");
        editor.getSelectorFormato().seleccionarFormato("MARKDOWN");
        editor.getBotonExportar().click();
        System.out.println();

        // ======================= ESCENARIO 3: la CHAIN rechaza =======================
        titulo("ESCENARIO 3 – Palabra prohibida (la cadena corta en el eslabón 2)");
        ReporteEjecutivoBuilder malo1 = new ReporteEjecutivoBuilder(new PdfRenderEngine(), fabrica);
        editor.setBuilder(malo1);
        malo1.addHeader("Oferta").addParagraph("Esto es puro spam, compre ya");
        editor.getBotonExportar().click();
        System.out.println();

        titulo("ESCENARIO 4 – Sintaxis inválida (la cadena corta en el eslabón 1)");
        ReporteEjecutivoBuilder malo2 = new ReporteEjecutivoBuilder(new PdfRenderEngine(), fabrica);
        editor.setBuilder(malo2);
        malo2.addHeader("Cálculo").addParagraph("Resultado: ${(precio * unidades}");
        editor.getBotonExportar().click();
        System.out.println();

        titulo("ESCENARIO 5 – Variable inexistente (rechaza el INTERPRETER dentro del eslabón 3)");
        ReporteEjecutivoBuilder malo3 = new ReporteEjecutivoBuilder(new PdfRenderEngine(), fabrica);
        editor.setBuilder(malo3);
        malo3.addHeader("Cálculo").addParagraph("Descuento: ${precio * descuento}");
        editor.getBotonExportar().click();
        System.out.println();

        // ======================= FLYWEIGHT: ahorro de memoria =======================
        titulo("FLYWEIGHT – Resumen de memoria");
        System.out.println("   Objetos pedidos a la fábrica : " + fabrica.getSolicitudes());
        System.out.println("   Flyweights realmente creados : " + fabrica.getTotalFlyweights());
        System.out.println("   Instancias evitadas          : "
                + (fabrica.getSolicitudes() - fabrica.getTotalFlyweights()));

        System.out.println("\n   Muestra de dibujo (traza activada) de los 3 primeros elementos del reporte:");
        FlyweightFactory.trazaDibujo = true;
        int n = 0;
        for (ElementoVisual e : reporte.build().getElementos()) {
            e.dibujar();
            if (++n == 3) break;
        }
        FlyweightFactory.trazaDibujo = false;
    }

    private static void titulo(String texto) {
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println(texto);
        System.out.println("════════════════════════════════════════════════════════════");
    }
}
