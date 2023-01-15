package cr.ac.cenfotec.appostado.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quiniela.
 */
@Entity
@Table(name = "tabla_quiniela")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quiniela implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @NotNull
    @Column(name = "costo_participacion", nullable = false)
    private Float costoParticipacion;

    @NotNull
    @Column(name = "primer_premio", nullable = false)
    private Float primerPremio;

    @NotNull
    @Column(name = "segundo_premio", nullable = false)
    private Float segundoPremio;

    @NotNull
    @Column(name = "tercer_premio", nullable = false)
    private Float tercerPremio;

    @NotNull
    @Size(max = 20)
    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quiniela id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Quiniela nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Quiniela descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getCostoParticipacion() {
        return this.costoParticipacion;
    }

    public Quiniela costoParticipacion(Float costoParticipacion) {
        this.setCostoParticipacion(costoParticipacion);
        return this;
    }

    public void setCostoParticipacion(Float costoParticipacion) {
        this.costoParticipacion = costoParticipacion;
    }

    public Float getPrimerPremio() {
        return this.primerPremio;
    }

    public Quiniela primerPremio(Float primerPremio) {
        this.setPrimerPremio(primerPremio);
        return this;
    }

    public void setPrimerPremio(Float primerPremio) {
        this.primerPremio = primerPremio;
    }

    public Float getSegundoPremio() {
        return this.segundoPremio;
    }

    public Quiniela segundoPremio(Float segundoPremio) {
        this.setSegundoPremio(segundoPremio);
        return this;
    }

    public void setSegundoPremio(Float segundoPremio) {
        this.segundoPremio = segundoPremio;
    }

    public Float getTercerPremio() {
        return this.tercerPremio;
    }

    public Quiniela tercerPremio(Float tercerPremio) {
        this.setTercerPremio(tercerPremio);
        return this;
    }

    public void setTercerPremio(Float tercerPremio) {
        this.tercerPremio = tercerPremio;
    }

    public String getEstado() {
        return this.estado;
    }

    public Quiniela estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiniela)) {
            return false;
        }
        return id != null && id.equals(((Quiniela) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quiniela{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", costoParticipacion=" + getCostoParticipacion() +
            ", primerPremio=" + getPrimerPremio() +
            ", segundoPremio=" + getSegundoPremio() +
            ", tercerPremio=" + getTercerPremio() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
