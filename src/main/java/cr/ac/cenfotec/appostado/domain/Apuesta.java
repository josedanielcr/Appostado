package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Apuesta.
 */
@Entity
@Table(name = "tabla_apuesta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Apuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creditos_apostados", nullable = false)
    private Float creditosApostados;

    @Column(name = "ha_ganado")
    private Boolean haGanado;

    @NotNull
    @Size(max = 20)
    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @OneToOne
    @JoinColumn(unique = true)
    private Competidor apostado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Usuario usuario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ganador", "deporte", "division", "competidor1", "competidor2", "quiniela" }, allowSetters = true)
    private Evento evento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Apuesta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCreditosApostados() {
        return this.creditosApostados;
    }

    public Apuesta creditosApostados(Float creditosApostados) {
        this.setCreditosApostados(creditosApostados);
        return this;
    }

    public void setCreditosApostados(Float creditosApostados) {
        this.creditosApostados = creditosApostados;
    }

    public Boolean getHaGanado() {
        return this.haGanado;
    }

    public Apuesta haGanado(Boolean haGanado) {
        this.setHaGanado(haGanado);
        return this;
    }

    public void setHaGanado(Boolean haGanado) {
        this.haGanado = haGanado;
    }

    public String getEstado() {
        return this.estado;
    }

    public Apuesta estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Competidor getApostado() {
        return this.apostado;
    }

    public void setApostado(Competidor competidor) {
        this.apostado = competidor;
    }

    public Apuesta apostado(Competidor competidor) {
        this.setApostado(competidor);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Apuesta usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Evento getEvento() {
        return this.evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Apuesta evento(Evento evento) {
        this.setEvento(evento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Apuesta)) {
            return false;
        }
        return id != null && id.equals(((Apuesta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Apuesta{" +
            "id=" + getId() +
            ", creditosApostados=" + getCreditosApostados() +
            ", haGanado='" + getHaGanado() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
