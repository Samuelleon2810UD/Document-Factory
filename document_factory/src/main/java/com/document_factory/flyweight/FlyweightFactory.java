package com.document_factory.flyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FLYWEIGHT – Fábrica que garantiza UNA sola instancia por glifo/icono.
 *
 * COMBINACIÓN DE PATRONES: los BUILDERS usan esta fábrica mientras construyen
 * el documento, de modo que miles de ElementoVisual (los "contextos") apunten
 * a unos pocos flyweights. Así Builder decide QUÉ se construye y Flyweight
 * decide CÓMO se comparte la memoria de lo construido.
 */
public class FlyweightFactory {

    /** Activa/desactiva la traza de dibujo (solo para la demo). */
    public static boolean trazaDibujo = false;

    private final Map<String, ElementoFlyweight> flyweights = new HashMap<>();
    private int solicitudes = 0;

    public ElementoFlyweight getCaracter(char c, String fuente) {
        solicitudes++;
        String clave = "C:" + fuente + ":" + c;
        return flyweights.computeIfAbsent(clave, k -> new CaracterFlyweight(c, fuente));
    }

    public ElementoFlyweight getIcono(String nombre) {
        solicitudes++;
        // Se simula una imagen "pesada": justo lo que conviene compartir.
        return flyweights.computeIfAbsent("I:" + nombre, k -> new IconoFlyweight(nombre, new byte[2048]));
    }

    /**
     * Convierte un texto en una lista de ElementoVisual (uno por carácter visible).
     * Cada ElementoVisual guarda solo el estado extrínseco; el glifo es compartido.
     */
    public List<ElementoVisual> crearTexto(String texto, String fuente, int xInicial, int y,
                                           String color, float escala) {
        List<ElementoVisual> resultado = new ArrayList<>();
        int x = xInicial;
        int avance = Math.max(1, Math.round(8 * escala));
        for (char c : texto.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                resultado.add(new ElementoVisual(x, y, color, escala, getCaracter(c, fuente)));
            }
            x += avance;
        }
        return resultado;
    }

    /** Cantidad de flyweights realmente creados (instancias únicas). */
    public int getTotalFlyweights() {
        return flyweights.size();
    }

    /** Cantidad de veces que se pidió un flyweight (instancias que habría habido sin el patrón). */
    public int getSolicitudes() {
        return solicitudes;
    }
}
