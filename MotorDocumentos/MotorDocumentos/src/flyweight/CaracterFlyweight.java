package flyweight;

// ConcreteFlyweight: estado intrínseco = carácter + tipografía base.
public class CaracterFlyweight implements ElementoFlyweight {
    private final char caracter;
    private final String tipografiaBase;

    public CaracterFlyweight(char caracter, String tipografiaBase) {
        this.caracter = caracter;
        this.tipografiaBase = tipografiaBase;
    }

    @Override
    public void dibujar(int posX, int posY, String color, float escala) {
        System.out.printf("  [Caracter '%c' | %s] x=%d y=%d color=%s escala=%.1f%n",
                caracter, tipografiaBase, posX, posY, color, escala);
    }
}
