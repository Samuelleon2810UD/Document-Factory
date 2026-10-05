package interpreter;

// TerminalExpression: variable resuelta desde el Contexto.
public class ExpresionVariable implements Expresion {
    private final String nombre;

    public ExpresionVariable(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public double interpretar(Contexto contexto) {
        return contexto.obtenerVariable(nombre);
    }
}
