package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Amigo.
 */
@Entity
@Table(name = "tabla_amigo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Amigo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @NotNull
    @Column(name = "id_amigo", nullable = false)
    private Long idAmigo;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "cuenta" }, allowSetters = true)
    private Usuario usuario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "cuenta" }, allowSetters = true)
    private Usuario amigo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Amigo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return this.idUsuario;
    }

    public Amigo idUsuario(Long idUsuario) {
        this.setIdUsuario(idUsuario);
        return this;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdAmigo() {
        return this.idAmigo;
    }

    public Amigo idAmigo(Long idAmigo) {
        this.setIdAmigo(idAmigo);
        return this;
    }

    public void setIdAmigo(Long idAmigo) {
        this.idAmigo = idAmigo;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Amigo usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Usuario getAmigo() {
        return this.amigo;
    }

    public void setAmigo(Usuario usuario) {
        this.amigo = usuario;
    }

    public Amigo amigo(Usuario usuario) {
        this.setAmigo(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Amigo)) {
            return false;
        }
        return id != null && id.equals(((Amigo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Amigo{" +
            "id=" + getId() +
            ", idUsuario=" + getIdUsuario() +
            ", idAmigo=" + getIdAmigo() +
            "}";
    }
}
