package interpreter;

// NonTerminalExpression: operación '*' entre dos sub-expresiones.
public class ExpresionMultiplicacion implements Expresion {
    private final Expresion izquierda;
    private final Expresion derecha;

    public ExpresionMultiplicacion(Expresion izquierda, Expresion derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public double interpretar(Contexto contexto) {
        return izquierda.interpretar(contexto) * derecha.interpretar(contexto);
    }
}
