package cr.ac.cenfotec.appostado.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Producto.
 */
@Entity
@Table(name = "tabla_producto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Producto implements Serializable {

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
    @Column(name = "costo", nullable = false)
    private Float costo;

    @NotNull
    @Size(max = 200)
    @Column(name = "foto", length = 200, nullable = false)
    private String foto;

    @Size(max = 50)
    @Column(name = "codigo_fijo", length = 50)
    private String codigoFijo;

    @Column(name = "num_compras")
    private Integer numCompras;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Producto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Producto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Producto descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getCosto() {
        return this.costo;
    }

    public Producto costo(Float costo) {
        this.setCosto(costo);
        return this;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public String getFoto() {
        return this.foto;
    }

    public Producto foto(String foto) {
        this.setFoto(foto);
        return this;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCodigoFijo() {
        return this.codigoFijo;
    }

    public Producto codigoFijo(String codigoFijo) {
        this.setCodigoFijo(codigoFijo);
        return this;
    }

    public void setCodigoFijo(String codigoFijo) {
        this.codigoFijo = codigoFijo;
    }

    public Integer getNumCompras() {
        return this.numCompras;
    }

    public Producto numCompras(Integer numCompras) {
        this.setNumCompras(numCompras);
        return this;
    }

    public void setNumCompras(Integer numCompras) {
        this.numCompras = numCompras;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return id != null && id.equals(((Producto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", costo=" + getCosto() +
            ", foto='" + getFoto() + "'" +
            ", codigoFijo='" + getCodigoFijo() + "'" +
            ", numCompras=" + getNumCompras() +
            "}";
    }
}
