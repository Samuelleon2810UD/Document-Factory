package com.document_factory.flyweight;

/**
 * FLYWEIGHT – "Contexto": guarda el estado EXTRÍNSECO (posición, color, escala)
 * y una referencia al flyweight compartido que aporta el estado intrínseco.
 */
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

    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public String getColor() { return color; }
    public float getEscala() { return escala; }
}
