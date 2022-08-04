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

    @NotNull
    @Size(max = 15)
    @Column(name = "tipo", length = 15, nullable = false)
    private String tipo;

    @Size(max = 50)
    @Column(name = "opcion_1", length = 50)
    private String opcion1;

    @Size(max = 50)
    @Column(name = "opcion_2", length = 50)
    private String opcion2;

    @Size(max = 50)
    @Column(name = "opcion_3", length = 50)
    private String opcion3;

    @Size(max = 50)
    @Column(name = "opcion_4", length = 50)
    private String opcion4;

    @Size(max = 125)
    @Column(name = "enlace", length = 125)
    private String enlace;

    @Column(name = "opcion_correcta")
    private Integer opcionCorrecta;

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

    public String getTipo() {
        return this.tipo;
    }

    public Mision tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOpcion1() {
        return this.opcion1;
    }

    public Mision opcion1(String opcion1) {
        this.setOpcion1(opcion1);
        return this;
    }

    public void setOpcion1(String opcion1) {
        this.opcion1 = opcion1;
    }

    public String getOpcion2() {
        return this.opcion2;
    }

    public Mision opcion2(String opcion2) {
        this.setOpcion2(opcion2);
        return this;
    }

    public void setOpcion2(String opcion2) {
        this.opcion2 = opcion2;
    }

    public String getOpcion3() {
        return this.opcion3;
    }

    public Mision opcion3(String opcion3) {
        this.setOpcion3(opcion3);
        return this;
    }

    public void setOpcion3(String opcion3) {
        this.opcion3 = opcion3;
    }

    public String getOpcion4() {
        return this.opcion4;
    }

    public Mision opcion4(String opcion4) {
        this.setOpcion4(opcion4);
        return this;
    }

    public void setOpcion4(String opcion4) {
        this.opcion4 = opcion4;
    }

    public String getEnlace() {
        return this.enlace;
    }

    public Mision enlace(String enlace) {
        this.setEnlace(enlace);
        return this;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public Integer getOpcionCorrecta() {
        return this.opcionCorrecta;
    }

    public Mision opcionCorrecta(Integer opcionCorrecta) {
        this.setOpcionCorrecta(opcionCorrecta);
        return this;
    }

    public void setOpcionCorrecta(Integer opcionCorrecta) {
        this.opcionCorrecta = opcionCorrecta;
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
            ", tipo='" + getTipo() + "'" +
            ", opcion1='" + getOpcion1() + "'" +
            ", opcion2='" + getOpcion2() + "'" +
            ", opcion3='" + getOpcion3() + "'" +
            ", opcion4='" + getOpcion4() + "'" +
            ", enlace='" + getEnlace() + "'" +
            ", opcionCorrecta=" + getOpcionCorrecta() +
            "}";
    }
}
