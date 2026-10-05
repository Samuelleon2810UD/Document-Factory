package interpreter;

public class ExpresionInvalidaException extends RuntimeException {
    public ExpresionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
