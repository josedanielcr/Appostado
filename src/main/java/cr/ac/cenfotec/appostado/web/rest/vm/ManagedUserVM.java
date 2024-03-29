package cr.ac.cenfotec.appostado.web.rest.vm;

import cr.ac.cenfotec.appostado.service.dto.AdminUserDTO;
import java.time.LocalDate;
import javax.validation.constraints.Size;

/**
 * View Model extending the AdminUserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends AdminUserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private LocalDate fechaNacimiento;

    private String pais;

    private String activationURL;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public String getActivationURL() {
        return activationURL;
    }

    public void setActivationURL(String activationEndpoint) {
        this.activationURL = activationEndpoint;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
