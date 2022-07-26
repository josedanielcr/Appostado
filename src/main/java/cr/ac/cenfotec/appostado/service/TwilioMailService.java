package cr.ac.cenfotec.appostado.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TwilioMailService {

    private static final Logger logger = LoggerFactory.getLogger(TwilioMailService.class);

    /**
     *
     * @param email Email address of recipient
     * @param name Application username
     * @param key URL link to activate account
     * @throws IOException
     */
    public void sendActivationMail(String email, String name, String key) throws IOException {
        Mail mail = new Mail();
        Email sender = new Email("appostado@gmail.com");
        mail.setFrom(sender);
        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("name", name);
        personalization.addDynamicTemplateData("key", key);
        personalization.addTo(new Email(email));
        mail.addPersonalization(personalization);
        mail.setTemplateId("d-17baba1f8840452bb6daac11580b9c35");
        sendInternal(mail);
    }

    /**
     *
     * @param email Email address of recipient
     * @param name Application username
     * @param key URL link to access reset account option
     * @throws IOException
     */
    public void sendResetPasswordMail(String email, String name, String key) throws IOException {
        Mail mail = new Mail();
        Email sender = new Email("appostado@gmail.com");
        mail.setFrom(sender);
        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("name", name);
        personalization.addDynamicTemplateData("key", key);
        personalization.addTo(new Email(email));
        mail.addPersonalization(personalization);
        mail.setTemplateId("d-d92b07beebd04b1e8460bcc38178f8d4");
        sendInternal(mail);
    }

    /**
     *
     * @param email Email address of recipient
     * @param name Application username
     * @param product Details of purchased producto
     * @param code Promotional code to claim digital money
     * @throws IOException
     */
    public void sendRedeemCodeMail(String email, String name, String product, String code) throws IOException {
        Mail mail = new Mail();
        Email sender = new Email("appostado@gmail.com");
        mail.setFrom(sender);
        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("name", name);
        personalization.addDynamicTemplateData("product", product);
        personalization.addDynamicTemplateData("code", code);
        personalization.addTo(new Email(email));
        mail.addPersonalization(personalization);
        mail.setTemplateId("d-3a810f840bea4f16959f8348b3170de4");
        sendInternal(mail);
    }

    /**
     *
     * @param email Email address of recipient
     * @param name Application username
     * @param prize Details of claimed prize
     * @param content Message from admin about where and how to claim prize locally
     * @throws IOException
     */
    public void sendPrizeDetailsMail(String email, String name, String content) throws IOException {
        Mail mail = new Mail();
        Email sender = new Email("appostado@gmail.com");
        mail.setFrom(sender);
        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("name", name);
        personalization.addDynamicTemplateData("content", content);
        personalization.addTo(new Email(email));
        mail.addPersonalization(personalization);
        mail.setTemplateId("d-68f79a2ae2f8471bb484a7a12fc8630b");
        sendInternal(mail);
    }

    /**
     *
     * @param mail Mail object to send through Sendgrid
     * @return String with the result of request
     * @throws IOException
     */
    private String sendInternal(Mail mail) throws IOException {
        logger.info("Sending email with subject {}", mail.subject);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            SendGrid sg = new SendGrid("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");
            Response response = sg.api(request);
            logger.info(response.getBody());
            return response.getBody();
        } catch (IOException ex) {
            throw ex;
        }
    }
}
