package cr.ac.cenfotec.appostado.web.rest.vm;

import java.time.LocalDate;

public class HistorialApuestaVM {

    private String resultado;
    private float creditos;
    private String descripcion;
    private LocalDate fecha;
    private String apostado;
    private String estado;

    public HistorialApuestaVM() {}

    public HistorialApuestaVM(String resultado, float creditos, String descripcion, LocalDate fecha, String apostado, String estado) {
        this.resultado = resultado;
        this.creditos = creditos;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.apostado = apostado;
        this.estado = estado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public float getCreditos() {
        return creditos;
    }

    public void setCreditos(float creditos) {
        this.creditos = creditos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getApostado() {
        return apostado;
    }

    public void setApostado(String apostado) {
        this.apostado = apostado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
