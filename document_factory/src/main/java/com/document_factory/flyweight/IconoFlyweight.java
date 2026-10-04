package com.document_factory.flyweight;

/** FLYWEIGHT concreto: un icono con su imagen (estado intrínseco pesado) compartida. */
public class IconoFlyweight implements ElementoFlyweight {

    private final String nombreIcono;
    private final byte[] bytesImagen;

    public IconoFlyweight(String nombreIcono, byte[] bytesImagen) {
        this.nombreIcono = nombreIcono;
        this.bytesImagen = bytesImagen;
    }

    @Override
    public void dibujar(int posX, int posY, String color, float escala) {
        if (FlyweightFactory.trazaDibujo) {
            System.out.printf("      · icono '%s' (%d bytes) en (%d,%d) color=%s escala=%.1f%n",
                    nombreIcono, bytesImagen.length, posX, posY, color, escala);
        }
    }
}
