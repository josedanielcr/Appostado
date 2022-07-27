package cr.ac.cenfotec.appostado.domain;

public class Ranking implements Comparable<Ranking> {

    private int posicion;

    private String nombreJugador;

    private float recordNeto;

    private String nacionalidad;

    private int totalGanadas;

    private int totalPerdidas;

    private int totalCanjes;

    private double rendimiento;

    public Ranking() {}

    public Ranking(
        int posicion,
        String nombreJugador,
        float recordNeto,
        String nacionalidad,
        int totalGanadas,
        int totalPeridas,
        int totalCanjes,
        double rendimiento
    ) {
        this.posicion = posicion;
        this.nombreJugador = nombreJugador;
        this.recordNeto = recordNeto;
        this.nacionalidad = nacionalidad;
        this.totalGanadas = totalGanadas;
        this.totalPerdidas = totalPeridas;
        this.totalCanjes = totalCanjes;
        this.rendimiento = rendimiento;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public float getRecordNeto() {
        return recordNeto;
    }

    public void setRecordNeto(float recordNeto) {
        this.recordNeto = recordNeto;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public int getTotalGanadas() {
        return totalGanadas;
    }

    public void setTotalGanadas(int totalGanadas) {
        this.totalGanadas = totalGanadas;
    }

    public int gettotalPerdidas() {
        return totalPerdidas;
    }

    public void settotalPerdidas(int totalPerdidas) {
        this.totalPerdidas = totalPerdidas;
    }

    public int getTotalCanjes() {
        return totalCanjes;
    }

    public void setTotalCanjes(int totalCanjes) {
        this.totalCanjes = totalCanjes;
    }

    public double getRendimiento() {
        return rendimiento;
    }

    public void setRendimiento(double rendimiento) {
        this.rendimiento = rendimiento;
    }

    @Override
    public int compareTo(Ranking o) {
        if (this.getRecordNeto() == o.getRecordNeto()) {
            return 0;
        } else if (this.getRecordNeto() < o.getRecordNeto()) {
            return 1;
        } else {
            return -1;
        }
    }
}
