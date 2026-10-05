package mediator;

public class BarraDeHerramientasBuilder extends ComponenteUI {
    private String plantilla = "REPORTE";
    private String ultimoTipo;
    private String ultimoContenido;

    public void seleccionarPlantilla(String plantilla) {
        this.plantilla = plantilla;
        System.out.println("[UI] BarraDeHerramientas: plantilla " + plantilla);
        mediador.notificar(this, "PLANTILLA_CAMBIADA");
    }

    /** tipo: HEADER, PARAGRAPH, TABLE o FOOTER. En TABLE: filas con ';' y celdas con '|'. */
    public void agregarSeccion(String tipo, String contenido) {
        this.ultimoTipo = tipo;
        this.ultimoContenido = contenido;
        System.out.println("[UI] BarraDeHerramientas: agregar " + tipo);
        mediador.notificar(this, "SECCION_AGREGADA");
    }

    public void agregarSeccion(String tipo) {
        agregarSeccion(tipo, "");
    }

    public String getPlantilla() {
        return plantilla;
    }
    public String getUltimoTipo() {
        return ultimoTipo;
    }
    public String getUltimoContenido() {
        return ultimoContenido;
    }
}
