package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotNull
    @Column(name = "id_apuesta", nullable = false)
    private Long idApuesta;

    @NotNull
    @Column(name = "id_transaccion", nullable = false)
    private Long idTransaccion;

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

    public Long getIdApuesta() {
        return this.idApuesta;
    }

    public ApuestaTransaccion idApuesta(Long idApuesta) {
        this.setIdApuesta(idApuesta);
        return this;
    }

    public void setIdApuesta(Long idApuesta) {
        this.idApuesta = idApuesta;
    }

    public Long getIdTransaccion() {
        return this.idTransaccion;
    }

    public ApuestaTransaccion idTransaccion(Long idTransaccion) {
        this.setIdTransaccion(idTransaccion);
        return this;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
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
            ", idApuesta=" + getIdApuesta() +
            ", idTransaccion=" + getIdTransaccion() +
            "}";
    }
}
