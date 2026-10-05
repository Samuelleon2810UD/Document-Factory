package bridge;

//Contenido del documento (encabezado, párrafo, tabla o pie).
public class Bloque {
    public enum Tipo { HEADER, PARAGRAPH, TABLE, FOOTER }

    private final Tipo tipo;
    private String texto;

    public Bloque(Tipo tipo, String texto) {
        this.tipo = tipo;
        this.texto = texto;
    }

    public Tipo getTipo() {
        return tipo;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
}
