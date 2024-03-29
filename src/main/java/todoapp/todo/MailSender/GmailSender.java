package todoapp.todo.MailSender;

import todoapp.todo.DatabaseConnection;
import todoapp.todo.RegisterController;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class GmailSender {
    public static boolean sendMail(String from, String to, String subject, String htmlContent) throws MessagingException {
        boolean success = false;

        // logic
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        String user = "tododesktopapp";
        String password = System.getenv("TODO_GMAIL_PASSWORD"); //environment variable used
        System.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        System.setProperty("mail.smtp.ssl.ciphersuites", "TLS_AES_128_GCM_SHA256");


        // session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html");
            Transport.send(message);
            success = true;

        } catch (Exception e) {
            System.out.println(e.getMessage());

            e.printStackTrace();
        }

        return success;
    }

}
