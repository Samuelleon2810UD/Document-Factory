package com.document_factory.flyweight;

/**
 * FLYWEIGHT – Interfaz del objeto compartido.
 * El estado INTRÍNSECO (carácter, tipografía, bytes del icono) vive dentro del
 * flyweight; el estado EXTRÍNSECO (posición, color, escala) llega por parámetro.
 */
public interface ElementoFlyweight {
    void dibujar(int posX, int posY, String color, float escala);
}
