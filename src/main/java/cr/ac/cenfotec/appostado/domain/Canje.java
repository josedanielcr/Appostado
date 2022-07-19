package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Canje.
 */
@Entity
@Table(name = "tabla_canje")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Canje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "estado", length = 20)
    private String estado;

    @Size(max = 500)
    @Column(name = "detalle", length = 500)
    private String detalle;

    @ManyToOne
    private Premio premio;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cuenta" }, allowSetters = true)
    private Transaccion transaccion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Canje id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return this.estado;
    }

    public Canje estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public Canje detalle(String detalle) {
        this.setDetalle(detalle);
        return this;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Premio getPremio() {
        return this.premio;
    }

    public void setPremio(Premio premio) {
        this.premio = premio;
    }

    public Canje premio(Premio premio) {
        this.setPremio(premio);
        return this;
    }

    public Transaccion getTransaccion() {
        return this.transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

    public Canje transaccion(Transaccion transaccion) {
        this.setTransaccion(transaccion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Canje)) {
            return false;
        }
        return id != null && id.equals(((Canje) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Canje{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            ", detalle='" + getDetalle() + "'" +
            "}";
    }
}
