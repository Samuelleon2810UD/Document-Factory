package app;

import bridge.Documento;
import chain.EvaluadorExpresiones;
import chain.FiltroPalabrasProhibidas;
import chain.ProcesadorHandler;
import chain.ValidadorSintaxis;
import flyweight.ElementoVisual;
import flyweight.FlyweightFactory;
import interpreter.Contexto;
import interpreter.Expresion;
import interpreter.ExpresionMultiplicacion;
import interpreter.ExpresionNumero;
import interpreter.ExpresionResta;
import interpreter.ExpresionVariable;
import interpreter.ParserExpresiones;
import mediator.BarraDeHerramientasBuilder;
import mediator.BotonExportar;
import mediator.DocumentEditor;
import mediator.SelectorDeFormato;
import mediator.VistaPrevia;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
public class Main {

    public static void main(String[] args) {
        //Configuración base
        titulo("0. Configuración base (Contexto del Interpreter y Chain)");
        Contexto ctx = new Contexto();
        ctx.asignarVariable("PRECIO_BASE", 100000.0);
        ctx.asignarVariable("DESCUENTO", 5000.0);
        ctx.asignarTexto("FECHA_ACTUAL", LocalDate.now().toString());

        ProcesadorHandler cadena = new ValidadorSintaxis();
        cadena.setSiguiente(new FiltroPalabrasProhibidas(List.of("confidencial", "password")))
              .setSiguiente(new EvaluadorExpresiones(ctx));
        System.out.println("Cadena: ValidadorSintaxis -> FiltroPalabrasProhibidas -> EvaluadorExpresiones");

        FlyweightFactory fabrica = new FlyweightFactory();
        SelectorDeFormato selector = new SelectorDeFormato();
        BarraDeHerramientasBuilder barra = new BarraDeHerramientasBuilder();
        VistaPrevia vista = new VistaPrevia();
        BotonExportar boton = new BotonExportar();
        DocumentEditor editor = new DocumentEditor(selector, barra, vista, boton, fabrica, cadena, Path.of("salida"));

        //Edición / configuración vía Mediator
        titulo("1. Edición y configuración vía Mediator (los componentes no se conocen entre sí)");
        barra.seleccionarPlantilla("FACTURA");
        selector.seleccionarFormato("PDF");
        barra.agregarSeccion("HEADER", "Factura No. 001 - Fecha: #{FECHA_ACTUAL}");
        barra.agregarSeccion("PARAGRAPH", "Cliente: ACME S.A.S. - Nota confidencial, password=1234");
        barra.agregarSeccion("TABLE", "Producto|Cantidad|Valor;Licencia anual|1|#{PRECIO_BASE};Soporte|1|#{PRECIO_BASE * 0.1}");
        barra.agregarSeccion("PARAGRAPH", "Total a pagar: $#{PRECIO_BASE * 1.19 - DESCUENTO}");
        barra.agregarSeccion("FOOTER", "Gracias por su compra");

        //Builder + Flyweight
        titulo("2. Documento creado por el Builder con Flyweights");
        Documento doc = editor.getDocumentoActual();
        int totalElementos = doc.getElementos().size();
        int unicos = fabrica.getTotalInstancias();
        System.out.println("ElementoVisual creados : " + totalElementos);
        System.out.println("Flyweights únicos      : " + unicos);
        System.out.printf("Reutilización          : %.1f usos por flyweight%n", (double) totalElementos / unicos);
        System.out.println("¿'a' (Monospace) reutilizada? "
                + (fabrica.getCaracter('a', "Monospace") == fabrica.getCaracter('a', "Monospace")));
        System.out.println("Primeros elementos dibujados (estado extrínseco por elemento):");
        for (int i = 0; i < 3; i++) {
            ElementoVisual e = doc.getElementos().get(i);
            e.dibujar();
        }

        //Interpreter (independiente)
        titulo("3. Interpreter: árbol manual y parser");
        Expresion arbol = new ExpresionResta(
                new ExpresionMultiplicacion(new ExpresionVariable("PRECIO_BASE"), new ExpresionNumero(1.19)),
                new ExpresionVariable("DESCUENTO"));
        System.out.println("Árbol manual  : PRECIO_BASE * 1.19 - DESCUENTO = " + arbol.interpretar(ctx));
        System.out.println("Parser        : (PRECIO_BASE + 1000) * 2 = "
                + ParserExpresiones.parsear("(PRECIO_BASE + 1000) * 2").interpretar(ctx));

        //Chain + Bridge: exportar en 3 formatos
        titulo("4. Chain of Responsibility + Bridge: exportar en PDF");
        boton.click();

        titulo("5. Bridge: mismo documento, otro RenderEngine (HTML)");
        selector.seleccionarFormato("HTML");
        boton.click();

        titulo("6. Bridge: Markdown");
        selector.seleccionarFormato("MARKDOWN");
        boton.click();

        titulo("7. Bridge: otra abstracción (DocumentoPaginado) en Markdown");
        barra.seleccionarPlantilla("REPORTE");
        boton.click();

        //Error crítico
        titulo("8. Error crítico: la cadena se interrumpe");
        barra.agregarSeccion("PARAGRAPH", "Descuento aplicado: #{DESCUENTO *");
        boton.click();

        System.out.println("\nTotal flyweights únicos al finalizar: " + fabrica.getTotalInstancias());
    }

    private static void titulo(String t) {
        System.out.println(t);
    }
}
