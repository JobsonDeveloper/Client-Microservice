package br.com.user.micro.service.imp;

import br.com.user.micro.exceptions.ErrorSendingEmailException;
import br.com.user.micro.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${company.email}")
    private String companyEmail;

    @Value("${application.charset}")
    private String charset;

    @Override
    public void sendMessage(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, charset);
            helper.setFrom(companyEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new ErrorSendingEmailException("It was not possible send the message to user via E-Mail!");
        }
    }
}
