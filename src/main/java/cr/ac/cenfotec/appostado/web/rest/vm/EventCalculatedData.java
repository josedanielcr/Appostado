package cr.ac.cenfotec.appostado.web.rest.vm;

public class EventCalculatedData {

    public double ganaciaEstimada;
    public double multiplicadorCompetidor1;
    public double multiplicadorCompetidor2;
    public double multiplicadorEmpate;

    public EventCalculatedData() {}

    public EventCalculatedData(
        double ganaciaEstimada,
        double multiplicadorCompetidor1,
        double multiplicadorCompetidor2,
        double multiplicadorEmpate
    ) {
        this.ganaciaEstimada = ganaciaEstimada;
        this.multiplicadorCompetidor1 = multiplicadorCompetidor1;
        this.multiplicadorCompetidor2 = multiplicadorCompetidor2;
        this.multiplicadorEmpate = multiplicadorEmpate;
    }

    public double getGanaciaEstimada() {
        return ganaciaEstimada;
    }

    public void setGanaciaEstimada(double ganaciaEstimada) {
        this.ganaciaEstimada = ganaciaEstimada;
    }

    public double getMultiplicadorCompetidor1() {
        return multiplicadorCompetidor1;
    }

    public void setMultiplicadorCompetidor1(double multiplicadorCompetidor1) {
        this.multiplicadorCompetidor1 = multiplicadorCompetidor1;
    }

    public double getMultiplicadorCompetidor2() {
        return multiplicadorCompetidor2;
    }

    public void setMultiplicadorCompetidor2(double multiplicadorCompetidor2) {
        this.multiplicadorCompetidor2 = multiplicadorCompetidor2;
    }

    public double getMultiplicadorEmpate() {
        return multiplicadorEmpate;
    }

    public void setMultiplicadorEmpate(double multiplicadorEmpate) {
        this.multiplicadorEmpate = multiplicadorEmpate;
    }
}
