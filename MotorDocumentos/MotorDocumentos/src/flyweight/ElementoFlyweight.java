package flyweight;

// Flyweight: interfaz común. El estado extrínseco llega por parámetro.
public interface ElementoFlyweight {
    void dibujar(int posX, int posY, String color, float escala);
}
