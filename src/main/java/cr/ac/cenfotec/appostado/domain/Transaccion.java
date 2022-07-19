package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaccion.
 */
@Entity
@Table(name = "tabla_transaccion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_cuenta", nullable = false)
    private Long idCuenta;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Size(max = 20)
    @Column(name = "tipo", length = 20, nullable = false)
    private String tipo;

    @NotNull
    @Size(max = 200)
    @Column(name = "descripcion", length = 200, nullable = false)
    private String descripcion;

    @NotNull
    @Column(name = "monto", nullable = false)
    private Float monto;

    @ManyToOne
    @JsonIgnoreProperties(value = { "usuario" }, allowSetters = true)
    private CuentaUsuario cuenta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCuenta() {
        return this.idCuenta;
    }

    public Transaccion idCuenta(Long idCuenta) {
        this.setIdCuenta(idCuenta);
        return this;
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Transaccion fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Transaccion tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Transaccion descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getMonto() {
        return this.monto;
    }

    public Transaccion monto(Float monto) {
        this.setMonto(monto);
        return this;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public CuentaUsuario getCuenta() {
        return this.cuenta;
    }

    public void setCuenta(CuentaUsuario cuentaUsuario) {
        this.cuenta = cuentaUsuario;
    }

    public Transaccion cuenta(CuentaUsuario cuentaUsuario) {
        this.setCuenta(cuentaUsuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaccion)) {
            return false;
        }
        return id != null && id.equals(((Transaccion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaccion{" +
            "id=" + getId() +
            ", idCuenta=" + getIdCuenta() +
            ", fecha='" + getFecha() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", monto=" + getMonto() +
            "}";
    }
}
