package cr.ac.cenfotec.appostado.web.rest.vm;

import java.time.LocalDate;

public class NotificacionAmigoVM {

    private String login;
    private String avatar;
    private LocalDate fecha;
    private String country;

    public NotificacionAmigoVM() {}

    public NotificacionAmigoVM(String login, String avatar, LocalDate fecha, String country) {
        this.login = login;
        this.avatar = avatar;
        this.fecha = fecha;
        this.country = country;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
