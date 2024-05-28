package jar.StockValueApp.service;


import jar.StockValueApp.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class EmailSendingService{

    private final JavaMailSender mailSender;

    public void sendEmail(final String toEmail, final UserRequestDTO userRequestDTO) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abadjanovas@gmail.com");
        message.setTo(toEmail);
        message.setText(" Hello "+ userRequestDTO.getUserName() + ","
                + "\n\n Welcome aboard and thank you for choosing Stock Value App!" +
                " We're excited to have you join our community of savvy investors.. \n" +
                " Should you need any assistance or have any questions," +
                " feel free to reach out to us – we're here to help! \n\n" +
                " Happy investing,\n\n" +
                " The Stock Value App Team");
        message.setSubject("Stock value app");

        mailSender.send(message);
        log.info("Mail sent successfully.");
    }

}
