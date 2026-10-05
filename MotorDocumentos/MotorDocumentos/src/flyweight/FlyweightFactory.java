package flyweight;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// FlyweightFactory: crea cada instancia compartida una sola vez y la reutiliza.
public class FlyweightFactory {
    private final Map<String, ElementoFlyweight> flyweights = new HashMap<>();

    public ElementoFlyweight getCaracter(char c, String fuente) {
        String clave = "C:" + fuente + ":" + c;
        return flyweights.computeIfAbsent(clave, k -> new CaracterFlyweight(c, fuente));
    }

    public ElementoFlyweight getIcono(String nombre) {
        String clave = "I:" + nombre;
        return flyweights.computeIfAbsent(clave,
                k -> new IconoFlyweight(nombre, Arrays.copyOf(nombre.getBytes(), 1024)));
    }

    public int getTotalInstancias() {
        return flyweights.size();
    }
}
