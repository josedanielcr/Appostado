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

    @NotNull
    @Column(name = "id_premio", nullable = false)
    private Long idPremio;

    @NotNull
    @Column(name = "id_transaccion", nullable = false)
    private Long idTransaccion;

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

    public Long getIdPremio() {
        return this.idPremio;
    }

    public Canje idPremio(Long idPremio) {
        this.setIdPremio(idPremio);
        return this;
    }

    public void setIdPremio(Long idPremio) {
        this.idPremio = idPremio;
    }

    public Long getIdTransaccion() {
        return this.idTransaccion;
    }

    public Canje idTransaccion(Long idTransaccion) {
        this.setIdTransaccion(idTransaccion);
        return this;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
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
            ", idPremio=" + getIdPremio() +
            ", idTransaccion=" + getIdTransaccion() +
            "}";
    }
}
