package com.document_factory.chain;

import com.document_factory.bridge.Documento;

/**
 * CHAIN OF RESPONSIBILITY – Handler abstracto.
 *
 * Cada eslabón valida o transforma el Documento y decide si la tubería continúa.
 * Se combina con TEMPLATE METHOD: {@link #procesar} fija el recorrido de la cadena y
 * las subclases solo implementan {@link #ejecutarProcesamiento}.
 *
 * COMBINACIÓN DE PATRONES: la cadena opera sobre el Documento ya construido por el
 * Builder; uno de sus eslabones (EvaluadorExpresiones) delega en el INTERPRETER.
 */
public abstract class ProcesadorHandler {

    protected ProcesadorHandler siguiente;

    /** Devuelve el siguiente handler para poder encadenar: a.setSiguiente(b).setSiguiente(c). */
    public ProcesadorHandler setSiguiente(ProcesadorHandler siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public final boolean procesar(Documento doc) {
        if (!ejecutarProcesamiento(doc)) {
            return false; // se corta la cadena: el documento fue rechazado
        }
        return siguiente == null || siguiente.procesar(doc);
    }

    protected abstract boolean ejecutarProcesamiento(Documento doc);

    protected boolean rechazar(String motivo) {
        System.out.println("   ✖ [" + getClass().getSimpleName() + "] " + motivo);
        return false;
    }

    protected boolean aprobar(String detalle) {
        System.out.println("   ✔ [" + getClass().getSimpleName() + "] " + detalle);
        return true;
    }
}
