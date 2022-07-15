package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notificacion.
 */
@Entity
@Table(name = "tabla_notificacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @NotNull
    @Size(max = 100)
    @Column(name = "descripcion", length = 100, nullable = false)
    private String descripcion;

    @NotNull
    @Size(max = 20)
    @Column(name = "tipo", length = 20, nullable = false)
    private String tipo;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "ha_ganado")
    private Boolean haGanado;

    @Column(name = "ganancia")
    private Float ganancia;

    @Column(name = "fue_leida")
    private Boolean fueLeida;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "cuenta" }, allowSetters = true)
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notificacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return this.idUsuario;
    }

    public Notificacion idUsuario(Long idUsuario) {
        this.setIdUsuario(idUsuario);
        return this;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Notificacion descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Notificacion tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Notificacion fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getHaGanado() {
        return this.haGanado;
    }

    public Notificacion haGanado(Boolean haGanado) {
        this.setHaGanado(haGanado);
        return this;
    }

    public void setHaGanado(Boolean haGanado) {
        this.haGanado = haGanado;
    }

    public Float getGanancia() {
        return this.ganancia;
    }

    public Notificacion ganancia(Float ganancia) {
        this.setGanancia(ganancia);
        return this;
    }

    public void setGanancia(Float ganancia) {
        this.ganancia = ganancia;
    }

    public Boolean getFueLeida() {
        return this.fueLeida;
    }

    public Notificacion fueLeida(Boolean fueLeida) {
        this.setFueLeida(fueLeida);
        return this;
    }

    public void setFueLeida(Boolean fueLeida) {
        this.fueLeida = fueLeida;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Notificacion usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notificacion)) {
            return false;
        }
        return id != null && id.equals(((Notificacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notificacion{" +
            "id=" + getId() +
            ", idUsuario=" + getIdUsuario() +
            ", descripcion='" + getDescripcion() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", haGanado='" + getHaGanado() + "'" +
            ", ganancia=" + getGanancia() +
            ", fueLeida='" + getFueLeida() + "'" +
            "}";
    }
}
