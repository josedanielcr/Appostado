package cr.ac.cenfotec.appostado.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Evento.
 */
@Entity
@Table(name = "tabla_evento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_deporte", nullable = false)
    private Long idDeporte;

    @NotNull
    @Column(name = "id_division", nullable = false)
    private Long idDivision;

    @NotNull
    @Column(name = "id_competidor_1", nullable = false)
    private Long idCompetidor1;

    @NotNull
    @Column(name = "id_competidor_2", nullable = false)
    private Long idCompetidor2;

    @Column(name = "id_quiniela")
    private Long idQuiniela;

    @NotNull
    @Column(name = "id_ganador", nullable = false)
    private Long idGanador;

    @NotNull
    @Column(name = "marcador_1", nullable = false)
    private Integer marcador1;

    @NotNull
    @Column(name = "marcador_2", nullable = false)
    private Integer marcador2;

    @NotNull
    @Size(max = 20)
    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @NotNull
    @Column(name = "multiplicador", nullable = false)
    private Integer multiplicador;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private ZonedDateTime horaInicio;

    @NotNull
    @Column(name = "hora_fin", nullable = false)
    private ZonedDateTime horaFin;

    @OneToOne
    @JoinColumn(unique = true)
    private Deporte deporte;

    @OneToOne
    @JoinColumn(unique = true)
    private Division division;

    @OneToOne
    @JoinColumn(unique = true)
    private Competidor competidor1;

    @OneToOne
    @JoinColumn(unique = true)
    private Competidor competidor2;

    @ManyToOne
    private Quiniela quiniela;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDeporte() {
        return this.idDeporte;
    }

    public Evento idDeporte(Long idDeporte) {
        this.setIdDeporte(idDeporte);
        return this;
    }

    public void setIdDeporte(Long idDeporte) {
        this.idDeporte = idDeporte;
    }

    public Long getIdDivision() {
        return this.idDivision;
    }

    public Evento idDivision(Long idDivision) {
        this.setIdDivision(idDivision);
        return this;
    }

    public void setIdDivision(Long idDivision) {
        this.idDivision = idDivision;
    }

    public Long getIdCompetidor1() {
        return this.idCompetidor1;
    }

    public Evento idCompetidor1(Long idCompetidor1) {
        this.setIdCompetidor1(idCompetidor1);
        return this;
    }

    public void setIdCompetidor1(Long idCompetidor1) {
        this.idCompetidor1 = idCompetidor1;
    }

    public Long getIdCompetidor2() {
        return this.idCompetidor2;
    }

    public Evento idCompetidor2(Long idCompetidor2) {
        this.setIdCompetidor2(idCompetidor2);
        return this;
    }

    public void setIdCompetidor2(Long idCompetidor2) {
        this.idCompetidor2 = idCompetidor2;
    }

    public Long getIdQuiniela() {
        return this.idQuiniela;
    }

    public Evento idQuiniela(Long idQuiniela) {
        this.setIdQuiniela(idQuiniela);
        return this;
    }

    public void setIdQuiniela(Long idQuiniela) {
        this.idQuiniela = idQuiniela;
    }

    public Long getIdGanador() {
        return this.idGanador;
    }

    public Evento idGanador(Long idGanador) {
        this.setIdGanador(idGanador);
        return this;
    }

    public void setIdGanador(Long idGanador) {
        this.idGanador = idGanador;
    }

    public Integer getMarcador1() {
        return this.marcador1;
    }

    public Evento marcador1(Integer marcador1) {
        this.setMarcador1(marcador1);
        return this;
    }

    public void setMarcador1(Integer marcador1) {
        this.marcador1 = marcador1;
    }

    public Integer getMarcador2() {
        return this.marcador2;
    }

    public Evento marcador2(Integer marcador2) {
        this.setMarcador2(marcador2);
        return this;
    }

    public void setMarcador2(Integer marcador2) {
        this.marcador2 = marcador2;
    }

    public String getEstado() {
        return this.estado;
    }

    public Evento estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getMultiplicador() {
        return this.multiplicador;
    }

    public Evento multiplicador(Integer multiplicador) {
        this.setMultiplicador(multiplicador);
        return this;
    }

    public void setMultiplicador(Integer multiplicador) {
        this.multiplicador = multiplicador;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Evento fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public ZonedDateTime getHoraInicio() {
        return this.horaInicio;
    }

    public Evento horaInicio(ZonedDateTime horaInicio) {
        this.setHoraInicio(horaInicio);
        return this;
    }

    public void setHoraInicio(ZonedDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public ZonedDateTime getHoraFin() {
        return this.horaFin;
    }

    public Evento horaFin(ZonedDateTime horaFin) {
        this.setHoraFin(horaFin);
        return this;
    }

    public void setHoraFin(ZonedDateTime horaFin) {
        this.horaFin = horaFin;
    }

    public Deporte getDeporte() {
        return this.deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public Evento deporte(Deporte deporte) {
        this.setDeporte(deporte);
        return this;
    }

    public Division getDivision() {
        return this.division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Evento division(Division division) {
        this.setDivision(division);
        return this;
    }

    public Competidor getCompetidor1() {
        return this.competidor1;
    }

    public void setCompetidor1(Competidor competidor) {
        this.competidor1 = competidor;
    }

    public Evento competidor1(Competidor competidor) {
        this.setCompetidor1(competidor);
        return this;
    }

    public Competidor getCompetidor2() {
        return this.competidor2;
    }

    public void setCompetidor2(Competidor competidor) {
        this.competidor2 = competidor;
    }

    public Evento competidor2(Competidor competidor) {
        this.setCompetidor2(competidor);
        return this;
    }

    public Quiniela getQuiniela() {
        return this.quiniela;
    }

    public void setQuiniela(Quiniela quiniela) {
        this.quiniela = quiniela;
    }

    public Evento quiniela(Quiniela quiniela) {
        this.setQuiniela(quiniela);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evento)) {
            return false;
        }
        return id != null && id.equals(((Evento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evento{" +
            "id=" + getId() +
            ", idDeporte=" + getIdDeporte() +
            ", idDivision=" + getIdDivision() +
            ", idCompetidor1=" + getIdCompetidor1() +
            ", idCompetidor2=" + getIdCompetidor2() +
            ", idQuiniela=" + getIdQuiniela() +
            ", idGanador=" + getIdGanador() +
            ", marcador1=" + getMarcador1() +
            ", marcador2=" + getMarcador2() +
            ", estado='" + getEstado() + "'" +
            ", multiplicador=" + getMultiplicador() +
            ", fecha='" + getFecha() + "'" +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", horaFin='" + getHoraFin() + "'" +
            "}";
    }
}
