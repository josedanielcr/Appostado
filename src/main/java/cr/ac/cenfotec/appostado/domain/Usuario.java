package cr.ac.cenfotec.appostado.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Usuario.
 */
@Entity
@Table(name = "tabla_usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_cuenta", nullable = false, unique = true)
    private Long idCuenta;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre_usuario", length = 100, nullable = false, unique = true)
    private String nombreUsuario;

    @Size(max = 100)
    @Column(name = "nombre_perfil", length = 100)
    private String nombrePerfil;

    @NotNull
    @Size(max = 100)
    @Column(name = "pais", length = 100, nullable = false)
    private String pais;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToOne
    @JoinColumn(unique = true)
    private CuentaUsuario cuenta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCuenta() {
        return this.idCuenta;
    }

    public Usuario idCuenta(Long idCuenta) {
        this.setIdCuenta(idCuenta);
        return this;
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNombreUsuario() {
        return this.nombreUsuario;
    }

    public Usuario nombreUsuario(String nombreUsuario) {
        this.setNombreUsuario(nombreUsuario);
        return this;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombrePerfil() {
        return this.nombrePerfil;
    }

    public Usuario nombrePerfil(String nombrePerfil) {
        this.setNombrePerfil(nombrePerfil);
        return this;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public String getPais() {
        return this.pais;
    }

    public Usuario pais(String pais) {
        this.setPais(pais);
        return this;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Usuario fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Usuario user(User user) {
        this.setUser(user);
        return this;
    }

    public CuentaUsuario getCuenta() {
        return this.cuenta;
    }

    public void setCuenta(CuentaUsuario cuentaUsuario) {
        this.cuenta = cuentaUsuario;
    }

    public Usuario cuenta(CuentaUsuario cuentaUsuario) {
        this.setCuenta(cuentaUsuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", idCuenta=" + getIdCuenta() +
            ", nombreUsuario='" + getNombreUsuario() + "'" +
            ", nombrePerfil='" + getNombrePerfil() + "'" +
            ", pais='" + getPais() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            "}";
    }
}
