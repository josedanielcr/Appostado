package cr.ac.cenfotec.appostado.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Mision.
 */
@Entity
@Table(name = "tabla_mision")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Mision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 200)
    @Column(name = "descripcion", length = 200, nullable = false)
    private String descripcion;

    @NotNull
    @Column(name = "bono_creditos", nullable = false)
    private Float bonoCreditos;

    @NotNull
    @Size(max = 15)
    @Column(name = "dia", length = 15, nullable = false)
    private String dia;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mision id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Mision nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Mision descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getBonoCreditos() {
        return this.bonoCreditos;
    }

    public Mision bonoCreditos(Float bonoCreditos) {
        this.setBonoCreditos(bonoCreditos);
        return this;
    }

    public void setBonoCreditos(Float bonoCreditos) {
        this.bonoCreditos = bonoCreditos;
    }

    public String getDia() {
        return this.dia;
    }

    public Mision dia(String dia) {
        this.setDia(dia);
        return this;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mision)) {
            return false;
        }
        return id != null && id.equals(((Mision) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mision{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", bonoCreditos=" + getBonoCreditos() +
            ", dia='" + getDia() + "'" +
            "}";
    }
}
