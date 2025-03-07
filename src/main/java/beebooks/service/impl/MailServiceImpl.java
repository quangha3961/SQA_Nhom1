package beebooks.service.impl;

import beebooks.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender emailSender;


    @Override
    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.info("Send email failed: " + e.getMessage());
        }
    }
}
