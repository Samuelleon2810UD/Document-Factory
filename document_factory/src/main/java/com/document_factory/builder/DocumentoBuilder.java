package com.document_factory.builder;

import com.document_factory.bridge.Documento;

/**
 * BUILDER – Interfaz del constructor (API fluida).
 * Los componentes de la UI y el Main arman documentos sin conocer
 * si el resultado será paginado o continuo.
 */
public interface DocumentoBuilder {
    DocumentoBuilder addHeader(String texto);
    DocumentoBuilder addParagraph(String texto);
    DocumentoBuilder addTable(int filas, int columnas);
    DocumentoBuilder addFooter(String texto);
    Documento build();
}
