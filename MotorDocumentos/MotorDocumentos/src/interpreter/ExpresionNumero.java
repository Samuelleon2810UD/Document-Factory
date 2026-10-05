package interpreter;

// TerminalExpression: literal numérico.
public class ExpresionNumero implements Expresion {
    private final double valor;

    public ExpresionNumero(double valor) {
        this.valor = valor;
    }

    @Override
    public double interpretar(Contexto contexto) {
        return valor;
    }
}
