package cr.ac.cenfotec.appostado.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CuentaUsuario.
 */
@Entity
@Table(name = "tabla_cuenta_usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CuentaUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "balance", nullable = false)
    private Float balance;

    @NotNull
    @Column(name = "num_canjes", nullable = false)
    private Integer numCanjes;

    @NotNull
    @Column(name = "apuestas_totales", nullable = false)
    private Integer apuestasTotales;

    @NotNull
    @Column(name = "apuestas_ganadas", nullable = false)
    private Integer apuestasGanadas;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CuentaUsuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getBalance() {
        return this.balance;
    }

    public CuentaUsuario balance(Float balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Integer getNumCanjes() {
        return this.numCanjes;
    }

    public CuentaUsuario numCanjes(Integer numCanjes) {
        this.setNumCanjes(numCanjes);
        return this;
    }

    public void setNumCanjes(Integer numCanjes) {
        this.numCanjes = numCanjes;
    }

    public Integer getApuestasTotales() {
        return this.apuestasTotales;
    }

    public CuentaUsuario apuestasTotales(Integer apuestasTotales) {
        this.setApuestasTotales(apuestasTotales);
        return this;
    }

    public void setApuestasTotales(Integer apuestasTotales) {
        this.apuestasTotales = apuestasTotales;
    }

    public Integer getApuestasGanadas() {
        return this.apuestasGanadas;
    }

    public CuentaUsuario apuestasGanadas(Integer apuestasGanadas) {
        this.setApuestasGanadas(apuestasGanadas);
        return this;
    }

    public void setApuestasGanadas(Integer apuestasGanadas) {
        this.apuestasGanadas = apuestasGanadas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CuentaUsuario)) {
            return false;
        }
        return id != null && id.equals(((CuentaUsuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CuentaUsuario{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            ", numCanjes=" + getNumCanjes() +
            ", apuestasTotales=" + getApuestasTotales() +
            ", apuestasGanadas=" + getApuestasGanadas() +
            "}";
    }
}
