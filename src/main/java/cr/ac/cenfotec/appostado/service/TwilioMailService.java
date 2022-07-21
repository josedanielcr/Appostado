package cr.ac.cenfotec.appostado.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioMailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    public String sendTextEmail(String correo, String nombre, String llave, String tipo) throws IOException {
        // the sender email should be the same as we used to Create a Single Sender Verification
        Email from = new Email("appostado@gmail.com");
        String subject = nombre;
        Email to = new Email(correo);
        Content content = new Content("text/plain", llave);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info(response.getBody());
            return response.getBody();
        } catch (IOException ex) {
            throw ex;
        }
    }
}
