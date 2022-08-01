package cr.ac.cenfotec.appostado.web.rest.vm;

public class AmigoDetailsVM {

    private String login;
    private String perfil;
    private Float balance;
    private Integer totales;
    private Integer ganados;
    private String avatar;
    private String country;
    private Integer canjes;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Integer getTotales() {
        return totales;
    }

    public void setTotales(Integer totales) {
        this.totales = totales;
    }

    public Integer getGanados() {
        return ganados;
    }

    public void setGanados(Integer ganados) {
        this.ganados = ganados;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getCanjes() {
        return canjes;
    }

    public void setCanjes(Integer canjes) {
        this.canjes = canjes;
    }
}
