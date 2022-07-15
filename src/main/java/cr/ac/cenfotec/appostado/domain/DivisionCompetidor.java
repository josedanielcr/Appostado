package cr.ac.cenfotec.appostado.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DivisionCompetidor.
 */
@Entity
@Table(name = "tabla_division_competidor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DivisionCompetidor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_division", nullable = false)
    private Long idDivision;

    @NotNull
    @Column(name = "id_competidor", nullable = false)
    private Long idCompetidor;

    @ManyToOne
    private Division division;

    @ManyToOne
    private Competidor competidor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DivisionCompetidor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDivision() {
        return this.idDivision;
    }

    public DivisionCompetidor idDivision(Long idDivision) {
        this.setIdDivision(idDivision);
        return this;
    }

    public void setIdDivision(Long idDivision) {
        this.idDivision = idDivision;
    }

    public Long getIdCompetidor() {
        return this.idCompetidor;
    }

    public DivisionCompetidor idCompetidor(Long idCompetidor) {
        this.setIdCompetidor(idCompetidor);
        return this;
    }

    public void setIdCompetidor(Long idCompetidor) {
        this.idCompetidor = idCompetidor;
    }

    public Division getDivision() {
        return this.division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public DivisionCompetidor division(Division division) {
        this.setDivision(division);
        return this;
    }

    public Competidor getCompetidor() {
        return this.competidor;
    }

    public void setCompetidor(Competidor competidor) {
        this.competidor = competidor;
    }

    public DivisionCompetidor competidor(Competidor competidor) {
        this.setCompetidor(competidor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DivisionCompetidor)) {
            return false;
        }
        return id != null && id.equals(((DivisionCompetidor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DivisionCompetidor{" +
            "id=" + getId() +
            ", idDivision=" + getIdDivision() +
            ", idCompetidor=" + getIdCompetidor() +
            "}";
    }
}
