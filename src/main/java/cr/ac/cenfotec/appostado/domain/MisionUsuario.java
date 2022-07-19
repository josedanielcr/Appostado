package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MisionUsuario.
 */
@Entity
@Table(name = "tabla_mision_usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MisionUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "completado", nullable = false)
    private Boolean completado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Usuario usuario;

    @ManyToOne
    private Mision mision;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MisionUsuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCompletado() {
        return this.completado;
    }

    public MisionUsuario completado(Boolean completado) {
        this.setCompletado(completado);
        return this;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public MisionUsuario usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Mision getMision() {
        return this.mision;
    }

    public void setMision(Mision mision) {
        this.mision = mision;
    }

    public MisionUsuario mision(Mision mision) {
        this.setMision(mision);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MisionUsuario)) {
            return false;
        }
        return id != null && id.equals(((MisionUsuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MisionUsuario{" +
            "id=" + getId() +
            ", completado='" + getCompletado() + "'" +
            "}";
    }
}
