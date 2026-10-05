package builder;

import bridge.Bloque;
import bridge.Documento;
import flyweight.ElementoVisual;
import flyweight.FlyweightFactory;

// Lógica común: cada carácter se convierte en un ElementoVisual que reutiliza flyweights.
public abstract class AbstractDocumentoBuilder implements DocumentoBuilder {
    protected final Documento documento;
    protected final FlyweightFactory fabrica;
    private final String fuente;
    private final String colorEncabezado;
    private final String colorTexto;
    private final float escalaEncabezado;
    private final float escalaTexto;
    private int cursorY = 0;

    protected AbstractDocumentoBuilder(Documento documento, FlyweightFactory fabrica, String fuente, String colorEncabezado, String colorTexto, float escalaEncabezado, float escalaTexto) {
        this.documento = documento;
        this.fabrica = fabrica;
        this.fuente = fuente;
        this.colorEncabezado = colorEncabezado;
        this.colorTexto = colorTexto;
        this.escalaEncabezado = escalaEncabezado;
        this.escalaTexto = escalaTexto;
    }

    protected void agregarTexto(String texto, String color, float escala) {
        int x = 0;
        for (char c : texto.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                documento.agregarElemento(new ElementoVisual(x, cursorY, color, escala,
                        fabrica.getCaracter(c, fuente)));
            }
            x += (int) (8 * escala);
        }
        cursorY += (int) (16 * escala);
    }

    protected void agregarIcono(String nombre) {
        documento.agregarElemento(new ElementoVisual(0, cursorY, colorTexto, 1.0f, fabrica.getIcono(nombre)));
    }

    @Override
    public DocumentoBuilder addHeader(String texto) {
        documento.agregarBloque(new Bloque(Bloque.Tipo.HEADER, texto));
        agregarTexto(texto, colorEncabezado, escalaEncabezado);
        return this;
    }

    @Override
    public DocumentoBuilder addParagraph(String texto) {
        documento.agregarBloque(new Bloque(Bloque.Tipo.PARAGRAPH, texto));
        agregarTexto(texto, colorTexto, escalaTexto);
        return this;
    }

    @Override
    public DocumentoBuilder addTable(int filas, int columnas) {
        String[][] datos = new String[filas][columnas];
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                datos[f][c] = (f == 0 ? "Col" + (c + 1) : "Celda " + f + "." + (c + 1));
            }
        }
        return addTable(datos);
    }

    @Override
    public DocumentoBuilder addTable(String[][] datos) {
        StringBuilder sb = new StringBuilder();
        for (int f = 0; f < datos.length; f++) {
            if (f > 0) sb.append("\n");
            sb.append(String.join("|", datos[f]));
            for (String celda : datos[f]) agregarTexto(celda, colorTexto, escalaTexto);
        }
        documento.agregarBloque(new Bloque(Bloque.Tipo.TABLE, sb.toString()));
        return this;
    }

    @Override
    public DocumentoBuilder addFooter(String texto) {
        documento.agregarBloque(new Bloque(Bloque.Tipo.FOOTER, texto));
        agregarTexto(texto, colorTexto, 0.8f);
        return this;
    }

    @Override
    public Documento build() {
        return documento;
    }
}
