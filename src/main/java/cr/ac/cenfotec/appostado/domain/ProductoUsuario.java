package cr.ac.cenfotec.appostado.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductoUsuario.
 */
@Entity
@Table(name = "tabla_producto_usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @NotNull
    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @NotNull
    @Column(name = "reclamado", nullable = false)
    private Boolean reclamado;

    @NotNull
    @Size(max = 50)
    @Column(name = "codigo", length = 50, nullable = false)
    private String codigo;

    @ManyToOne
    private Producto producto;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductoUsuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return this.idUsuario;
    }

    public ProductoUsuario idUsuario(Long idUsuario) {
        this.setIdUsuario(idUsuario);
        return this;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdProducto() {
        return this.idProducto;
    }

    public ProductoUsuario idProducto(Long idProducto) {
        this.setIdProducto(idProducto);
        return this;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Boolean getReclamado() {
        return this.reclamado;
    }

    public ProductoUsuario reclamado(Boolean reclamado) {
        this.setReclamado(reclamado);
        return this;
    }

    public void setReclamado(Boolean reclamado) {
        this.reclamado = reclamado;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public ProductoUsuario codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ProductoUsuario producto(Producto producto) {
        this.setProducto(producto);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ProductoUsuario usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoUsuario)) {
            return false;
        }
        return id != null && id.equals(((ProductoUsuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoUsuario{" +
            "id=" + getId() +
            ", idUsuario=" + getIdUsuario() +
            ", idProducto=" + getIdProducto() +
            ", reclamado='" + getReclamado() + "'" +
            ", codigo='" + getCodigo() + "'" +
            "}";
    }
}
