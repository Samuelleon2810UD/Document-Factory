package flyweight;

// Contexto del Flyweight: guarda el estado extrínseco y referencia al flyweight compartido.
public class ElementoVisual {
    private final int posX;
    private final int posY;
    private final String color;
    private final float escala;
    private final ElementoFlyweight flyweight;

    public ElementoVisual(int posX, int posY, String color, float escala, ElementoFlyweight flyweight) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.escala = escala;
        this.flyweight = flyweight;
    }

    public void dibujar() {
        flyweight.dibujar(posX, posY, color, escala);
    }

    public ElementoFlyweight getFlyweight() {
        return flyweight;
    }
}
