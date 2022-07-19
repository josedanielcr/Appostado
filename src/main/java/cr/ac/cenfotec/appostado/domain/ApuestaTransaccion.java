package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApuestaTransaccion.
 */
@Entity
@Table(name = "tabla_apuesta_transaccion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApuestaTransaccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "apostado", "usuario", "evento" }, allowSetters = true)
    private Apuesta apuesta;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cuenta" }, allowSetters = true)
    private Transaccion transaccion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApuestaTransaccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Apuesta getApuesta() {
        return this.apuesta;
    }

    public void setApuesta(Apuesta apuesta) {
        this.apuesta = apuesta;
    }

    public ApuestaTransaccion apuesta(Apuesta apuesta) {
        this.setApuesta(apuesta);
        return this;
    }

    public Transaccion getTransaccion() {
        return this.transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

    public ApuestaTransaccion transaccion(Transaccion transaccion) {
        this.setTransaccion(transaccion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApuestaTransaccion)) {
            return false;
        }
        return id != null && id.equals(((ApuestaTransaccion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApuestaTransaccion{" +
            "id=" + getId() +
            "}";
    }
}
