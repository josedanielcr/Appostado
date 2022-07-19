package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LigaUsuario.
 */
@Entity
@Table(name = "tabla_liga_usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LigaUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @NotNull
    @Column(name = "id_liga", nullable = false)
    private Long idLiga;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Usuario usuario;

    @ManyToOne
    private Liga liga;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LigaUsuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return this.idUsuario;
    }

    public LigaUsuario idUsuario(Long idUsuario) {
        this.setIdUsuario(idUsuario);
        return this;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdLiga() {
        return this.idLiga;
    }

    public LigaUsuario idLiga(Long idLiga) {
        this.setIdLiga(idLiga);
        return this;
    }

    public void setIdLiga(Long idLiga) {
        this.idLiga = idLiga;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LigaUsuario usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Liga getLiga() {
        return this.liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    public LigaUsuario liga(Liga liga) {
        this.setLiga(liga);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigaUsuario)) {
            return false;
        }
        return id != null && id.equals(((LigaUsuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigaUsuario{" +
            "id=" + getId() +
            ", idUsuario=" + getIdUsuario() +
            ", idLiga=" + getIdLiga() +
            "}";
    }
}
