package com.document_factory.builder;

import java.util.List;

import com.document_factory.bridge.Documento;
import com.document_factory.bridge.Documento.TipoSeccion;
import com.document_factory.bridge.DocumentoContinuo;
import com.document_factory.bridge.RenderEngine;
import com.document_factory.flyweight.ElementoVisual;
import com.document_factory.flyweight.FlyweightFactory;

/**
 * BUILDER concreto – arma un {@link DocumentoContinuo} (estilo compacto, tipo ticket).
 * Misma interfaz que el ReporteEjecutivoBuilder, otro producto y otro estilo visual:
 * el cliente (DocumentEditor) no cambia, solo cambia el builder inyectado.
 */
public class FacturaSimpleBuilder implements DocumentoBuilder {

    private final DocumentoContinuo documento;
    private final FlyweightFactory fabrica;
    private int cursorY = 10;

    public FacturaSimpleBuilder(RenderEngine motorInicial, FlyweightFactory fabrica) {
        this.documento = new DocumentoContinuo(motorInicial);
        this.fabrica = fabrica;
    }

    @Override
    public DocumentoBuilder addHeader(String texto) {
        List<ElementoVisual> glifos = fabrica.crearTexto(texto, "Courier", 5, cursorY, "#000000", 1.2f);
        documento.agregarSeccion(TipoSeccion.HEADER, texto, glifos);
        cursorY += 15;
        return this;
    }

    @Override
    public DocumentoBuilder addParagraph(String texto) {
        List<ElementoVisual> glifos = fabrica.crearTexto(texto, "Courier", 5, cursorY, "#000000", 0.8f);
        documento.agregarSeccion(TipoSeccion.PARAGRAPH, texto, glifos);
        cursorY += 12;
        return this;
    }

    @Override
    public DocumentoBuilder addTable(int filas, int columnas) {
        StringBuilder datos = new StringBuilder();
        for (int f = 1; f <= filas; f++) {
            for (int c = 1; c <= columnas; c++) {
                datos.append("F").append(f).append("C").append(c);
                if (c < columnas) datos.append(",");
            }
            if (f < filas) datos.append(";");
        }
        List<ElementoVisual> glifos = fabrica.crearTexto(datos.toString(), "Courier", 5, cursorY, "#000000", 0.7f);
        documento.agregarSeccion(TipoSeccion.TABLE, datos.toString(), glifos);
        cursorY += 10 * filas + 5;
        return this;
    }

    @Override
    public DocumentoBuilder addFooter(String texto) {
        // Reutiliza el icono "logo-empresa" ya creado por otro builder: el Flyweight es global.
        documento.agregarElemento(new ElementoVisual(5, cursorY, "#555555", 0.5f, fabrica.getIcono("logo-empresa")));
        List<ElementoVisual> glifos = fabrica.crearTexto(texto, "Courier", 20, cursorY, "#555555", 0.7f);
        documento.agregarSeccion(TipoSeccion.FOOTER, texto, glifos);
        cursorY += 10;
        return this;
    }

    @Override
    public Documento build() {
        return documento;
    }
}
