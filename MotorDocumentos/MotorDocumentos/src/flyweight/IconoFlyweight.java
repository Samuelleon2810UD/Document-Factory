package flyweight;

// ConcreteFlyweight: estado intrínseco = nombre del icono + bytes de la imagen.
public class IconoFlyweight implements ElementoFlyweight {
    private final String nombreIcono;
    private final byte[] bytesImagen;

    public IconoFlyweight(String nombreIcono, byte[] bytesImagen) {
        this.nombreIcono = nombreIcono;
        this.bytesImagen = bytesImagen;
    }

    @Override
    public void dibujar(int posX, int posY, String color, float escala) {
        System.out.printf("  [Icono '%s' | %d bytes] x=%d y=%d color=%s escala=%.1f%n", nombreIcono, bytesImagen.length, posX, posY, color, escala);
    }
}
