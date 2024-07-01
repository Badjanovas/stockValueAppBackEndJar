package jar.StockValueApp.service;


import jar.StockValueApp.config.EmailProperties;
import jar.StockValueApp.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;

    public void sendEmail(final String toEmail, final UserRequestDTO userRequestDTO) {
        final var message = new SimpleMailMessage();
        message.setFrom("abadjanovas@gmail.com");
        message.setTo(toEmail);

        // Replace placeholder with actual value
        final var text = emailProperties.getTemplate().replace("{userName}", userRequestDTO.getUserName());

        message.setText(text);
        message.setSubject("Stock value app");

        mailSender.send(message);
        log.info("Mail sent successfully.");
    }
}
