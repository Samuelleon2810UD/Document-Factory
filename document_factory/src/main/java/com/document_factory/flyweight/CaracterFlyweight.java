package com.document_factory.flyweight;

/** FLYWEIGHT concreto: un glifo (carácter + tipografía) compartido por todo el documento. */
public class CaracterFlyweight implements ElementoFlyweight {

    private final char caracter;
    private final String tipografiaBase;

    public CaracterFlyweight(char caracter, String tipografiaBase) {
        this.caracter = caracter;
        this.tipografiaBase = tipografiaBase;
    }

    @Override
    public void dibujar(int posX, int posY, String color, float escala) {
        if (FlyweightFactory.trazaDibujo) {
            System.out.printf("      · glifo '%c' (%s) en (%d,%d) color=%s escala=%.1f%n",
                    caracter, tipografiaBase, posX, posY, color, escala);
        }
    }
}
