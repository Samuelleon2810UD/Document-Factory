package chain;

import bridge.Documento;

// Handler: si ejecutarProcesamiento() devuelve false (error crítico) la cadena se interrumpe.
public abstract class ProcesadorHandler {
    protected ProcesadorHandler siguiente;

    /** Devuelve el handler recibido para poder encadenar: a.setSiguiente(b).setSiguiente(c). */
    public ProcesadorHandler setSiguiente(ProcesadorHandler siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public boolean procesar(Documento doc) {
        if (!ejecutarProcesamiento(doc)) {
            return false;
        }
        return siguiente == null || siguiente.procesar(doc);
    }

    protected abstract boolean ejecutarProcesamiento(Documento doc);
}
