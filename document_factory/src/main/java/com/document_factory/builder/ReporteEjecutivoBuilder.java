package com.document_factory.builder;

import java.util.List;
import com.document_factory.bridge.Documento;
import com.document_factory.bridge.Documento.TipoSeccion;
import com.document_factory.bridge.DocumentoPaginado;
import com.document_factory.bridge.RenderEngine;
import com.document_factory.flyweight.ElementoVisual;
import com.document_factory.flyweight.FlyweightFactory;

/**
 * BUILDER concreto – arma un {@link DocumentoPaginado}.
 *
 * COMBINACIÓN DE PATRONES (el punto donde convergen tres):
 *  - BUILDER:   construye paso a paso (addHeader, addParagraph, ...).
 *  - FLYWEIGHT: cada texto se convierte en ElementoVisual que comparten glifos
 *               a través de la FlyweightFactory (no se duplican objetos).
 *  - BRIDGE:    el producto se crea ya "puenteado" a un RenderEngine, que luego
 *               puede cambiarse sin reconstruir nada.
 */
public class ReporteEjecutivoBuilder implements DocumentoBuilder {

    private final DocumentoPaginado documento;
    private final FlyweightFactory fabrica;
    private int cursorY = 20;

    public ReporteEjecutivoBuilder(RenderEngine motorInicial, FlyweightFactory fabrica) {
        this.documento = new DocumentoPaginado(motorInicial);
        this.fabrica = fabrica;
    }

    @Override
    public DocumentoBuilder addHeader(String texto) {
        // Icono compartido (Flyweight) + texto grande en azul corporativo.
        documento.agregarElemento(new ElementoVisual(10, cursorY, "#1F3A5F", 1.5f, fabrica.getIcono("logo-empresa")));
        List<ElementoVisual> glifos = fabrica.crearTexto(texto, "Georgia", 30, cursorY, "#1F3A5F", 1.5f);
        documento.agregarSeccion(TipoSeccion.HEADER, texto, glifos);
        cursorY += 40;
        return this;
    }

    @Override
    public DocumentoBuilder addParagraph(String texto) {
        List<ElementoVisual> glifos = fabrica.crearTexto(texto, "Georgia", 10, cursorY, "#000000", 1.0f);
        documento.agregarSeccion(TipoSeccion.PARAGRAPH, texto, glifos);
        cursorY += 30;
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
        List<ElementoVisual> glifos = fabrica.crearTexto(datos.toString(), "Georgia", 10, cursorY, "#333333", 0.9f);
        documento.agregarSeccion(TipoSeccion.TABLE, datos.toString(), glifos);
        cursorY += 20 * filas + 10;
        return this;
    }

    @Override
    public DocumentoBuilder addFooter(String texto) {
        List<ElementoVisual> glifos = fabrica.crearTexto(texto, "Georgia", 10, cursorY, "#777777", 0.8f);
        documento.agregarSeccion(TipoSeccion.FOOTER, texto, glifos);
        cursorY += 20;
        return this;
    }

    @Override
    public Documento build() {
        return documento;
    }
}
