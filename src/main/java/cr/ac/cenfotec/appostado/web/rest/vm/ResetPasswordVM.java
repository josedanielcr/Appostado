package cr.ac.cenfotec.appostado.web.rest.vm;

public class ResetPasswordVM {

    private String email;
    private String resetURL;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetURL() {
        return resetURL;
    }

    public void setResetURL(String resetURL) {
        this.resetURL = resetURL;
    }
}
