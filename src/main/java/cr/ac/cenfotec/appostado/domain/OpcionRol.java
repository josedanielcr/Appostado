package cr.ac.cenfotec.appostado.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OpcionRol.
 */
@Entity
@Table(name = "tabla_opcion_rol")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OpcionRol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "opcion", length = 20, nullable = false)
    private String opcion;

    @NotNull
    @Size(max = 50)
    @Column(name = "path", length = 50, nullable = false)
    private String path;

    @NotNull
    @Size(max = 30)
    @Column(name = "icono", length = 30, nullable = false)
    private String icono;

    @NotNull
    @Size(max = 13)
    @Column(name = "nombre", length = 13, nullable = false)
    private String nombre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OpcionRol id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpcion() {
        return this.opcion;
    }

    public OpcionRol opcion(String opcion) {
        this.setOpcion(opcion);
        return this;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getPath() {
        return this.path;
    }

    public OpcionRol path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcono() {
        return this.icono;
    }

    public OpcionRol icono(String icono) {
        this.setIcono(icono);
        return this;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getNombre() {
        return this.nombre;
    }

    public OpcionRol nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpcionRol)) {
            return false;
        }
        return id != null && id.equals(((OpcionRol) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OpcionRol{" +
            "id=" + getId() +
            ", opcion='" + getOpcion() + "'" +
            ", path='" + getPath() + "'" +
            ", icono='" + getIcono() + "'" +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
