package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MisionTransaccion.
 */
@Entity
@Table(name = "tabla_mision_transaccion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MisionTransaccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_mision", nullable = false)
    private Long idMision;

    @NotNull
    @Column(name = "id_transaccion", nullable = false)
    private Long idTransaccion;

    @ManyToOne
    private Mision mision;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cuenta" }, allowSetters = true)
    private Transaccion transaccion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MisionTransaccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdMision() {
        return this.idMision;
    }

    public MisionTransaccion idMision(Long idMision) {
        this.setIdMision(idMision);
        return this;
    }

    public void setIdMision(Long idMision) {
        this.idMision = idMision;
    }

    public Long getIdTransaccion() {
        return this.idTransaccion;
    }

    public MisionTransaccion idTransaccion(Long idTransaccion) {
        this.setIdTransaccion(idTransaccion);
        return this;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Mision getMision() {
        return this.mision;
    }

    public void setMision(Mision mision) {
        this.mision = mision;
    }

    public MisionTransaccion mision(Mision mision) {
        this.setMision(mision);
        return this;
    }

    public Transaccion getTransaccion() {
        return this.transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

    public MisionTransaccion transaccion(Transaccion transaccion) {
        this.setTransaccion(transaccion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MisionTransaccion)) {
            return false;
        }
        return id != null && id.equals(((MisionTransaccion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MisionTransaccion{" +
            "id=" + getId() +
            ", idMision=" + getIdMision() +
            ", idTransaccion=" + getIdTransaccion() +
            "}";
    }
}
