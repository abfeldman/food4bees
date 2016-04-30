package nl.food4bees.backend;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

import java.io.InputStream;
import java.io.IOException;

import java.util.Properties;
import java.util.Map;

import nl.food4bees.backend.Config;

public class Mailer
{
    private static Mailer instance;

    private Mailer()
    {
    }

    public static Mailer instance()
    {
        if (instance == null) {
            instance = new Mailer();
        }
        return instance;
    }

    public void sendMail(String template,
                         String recipientMail,
                         String recipientName,
                         String senderMail,
                         String senderName,
                         Map<String, String> params)
        throws AddressException, MessagingException, IOException
    {
        InputStream streamText = Mailer.class.getResourceAsStream("/main/resources/templates/" + template + ".txt");
        InputStream streamHTML = Mailer.class.getResourceAsStream("/main/resources/templates/" + template + ".html");
        InputStream streamSubject = Mailer.class.getResourceAsStream("/main/resources/templates/" + template + ".subject");

        String text = Util.streamToString(streamText);
        String HTML = Util.streamToString(streamHTML);
        String subject = Util.streamToString(streamSubject);

        text = text.replace("${recipientName}", recipientName);
        text = text.replace("${recipientMail}", recipientMail);
        text = text.replace("${senderName}", senderName);
        text = text.replace("${senderMail}", senderMail);
        HTML = HTML.replace("${recipientName}", recipientName);
        HTML = HTML.replace("${recipientMail}", recipientMail);
        HTML = HTML.replace("${senderName}", senderName);
        HTML = HTML.replace("${senderMail}", senderMail);
        for (Map.Entry<String, String> param : params.entrySet()) {
            String key = param.getKey();
            String value = param.getValue();

            text = text.replace("${" + key + "}", value);
            HTML = HTML.replace("${" + key + "}", value);
        }

        String to = recipientName + " <" + recipientMail + ">";
        String from = senderName + " <" + senderMail + ">";

        Config config = Config.instance();
        
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", config.getProperty("smtps_host"));
        properties.put("mail.smtps.port", config.getProperty("smtps_port"));
        // properties.put("mail.smtps.auth", "true");

        Session session = Session.getDefaultInstance(properties);
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart("alternative");

        MimeBodyPart textBodyPart = new MimeBodyPart();
        MimeBodyPart HTMLBodyPart = new MimeBodyPart();

        textBodyPart.setText(text, "utf-8");
        HTMLBodyPart.setContent(HTML, "text/html; charset=utf-8");

        multipart.addBodyPart(textBodyPart);
        multipart.addBodyPart(HTMLBodyPart);

        message.setContent(multipart);

        // Transport.send(message);

        Transport transport = session.getTransport("smtps");
        transport.connect(config.getProperty("smtps_host"),
                          config.getProperty("smtps_user"),
                          config.getProperty("smtps_password"));
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
