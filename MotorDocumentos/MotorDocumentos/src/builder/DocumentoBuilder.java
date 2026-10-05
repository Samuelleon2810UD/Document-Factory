package builder;

import bridge.Documento;

//Pasos paara ensamblar
public interface DocumentoBuilder {
    DocumentoBuilder addHeader(String texto);
    DocumentoBuilder addParagraph(String texto);
    DocumentoBuilder addTable(int filas, int columnas);
    DocumentoBuilder addTable(String[][] datos);
    DocumentoBuilder addFooter(String texto);
    Documento build();
}
