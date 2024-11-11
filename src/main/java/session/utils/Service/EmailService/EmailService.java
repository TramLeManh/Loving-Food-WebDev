package session.utils.Service.EmailService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
@Component
public class EmailService {
    //if email error return false
    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    //Implement Mail
    public boolean sendOTP(String username, String email, int otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String subject = "Reset Password OTP";
            helper.setFrom("webproject123@gmail.com");
            helper.setFrom(new InternetAddress("lemanh1412@gmail.com", "Dev Support"));
            String body = "<!DOCTYPE html>" + "<html lan" +
                    "g=\"en\">" + "<head>" + "<meta charset=\"UTF-8\">" + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" + "<title>OTP Email</title>" + "<style>" + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f6f6f6; }" + ".container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd; }" + ".header { background-color: #4CAF50; color: white; text-align: center; padding: 10px 0; }" + ".content { padding: 20px; }"
                    + ".footer { text-align: center; color: #888888; font-size: 12px; padding: 20px; margin-top: 20px; border-top: 1px solid #dddddd; }"
                    + "p { line-height: 1.6; }" + "</style>" + "</head>" + "<body>" + "<div class=\"container\">"
                    + "  <div class=\"header\">" + "    <h1>Dev</h1>" + "  </div>" + "  <div class=\"content\">"
                    + "    <h2>Password Reset OTP</h2>" + "    <p>Dear <strong>" + username + "</strong></p>"
                    + "    <p>Your OTP is: <strong style=\"color: red;\">" + otp + "</strong></p>" + "    <p>Do not share this OTP with anyone. This OTP is <strong style=\"color: red;\">valid for 10 minutes</strong> .</p>"
                    + "    <p>If you did not request this OTP, please contact our support team.</p>" + "    <p>Best regards,</p>"
                    + "    <p>The Dev Team</p>" + "  </div>" + "  <div class=\"footer\">" + "    <p>Dev Inc. &copy; 2024</p>"
                    + "    <p>This email was sent to " + email + ". If you have any issues, please contact <a href=\"mailto:lemanh1412@gmail.com\">support@Dev.com</a>.</p>"
                    + "  </div>" + "</div>" + "</body>" + "</html>";

            helper.setTo(email);
            helper.setText(body, true);
            helper.setSubject(subject);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean sendOTP(String email, int otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String subject = "Reset Password OTP";
            helper.setFrom("webproject123@gmail.com");
            helper.setFrom(new InternetAddress("lemanh1412@gmail.com", "Dev Support"));
            String body = "<!DOCTYPE html>" + "<html lan" +
                    "g=\"en\">" + "<head>" + "<meta charset=\"UTF-8\">" + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" + "<title>OTP Email</title>" + "<style>" + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f6f6f6; }" + ".container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd; }" + ".header { background-color: #4CAF50; color: white; text-align: center; padding: 10px 0; }" + ".content { padding: 20px; }"
                    + ".footer { text-align: center; color: #888888; font-size: 12px; padding: 20px; margin-top: 20px; border-top: 1px solid #dddddd; }"
                    + "p { line-height: 1.6; }" + "</style>" + "</head>" + "<body>" + "<div class=\"container\">"
                    + "  <div class=\"header\">" + "    <h1>Dev</h1>" + "  </div>" + "  <div class=\"content\">"
                    + "    <h2>Verify Email Address</h2>" + "    <p>Dear " + "user" + "</p>"
                    + "    <p>Your OTP is: <strong style=\"color: red;\">" + otp + "</strong></p>" + "    <p>Do not share this OTP with anyone. This OTP is <strong style=\"color: red;\">valid for 10 minutes</strong> .</p>"
                    + "    <p>If you did not request this OTP, please contact our support team.</p>" + "    <p>Best regards,</p>"
                    + "    <p>The Dev Team</p>" + "  </div>" + "  <div class=\"footer\">" + "    <p>Dev Inc. &copy; 2024</p>"
                    + "    <p>This email was sent to " + email + ". If you have any issues, please contact <a href=\"mailto:lemanh1412@gmail.com\">support@Dev.com</a>.</p>"
                    + "  </div>" + "</div>" + "</body>" + "</html>";

            helper.setTo(email);
            helper.setText(body, true);
            helper.setSubject(subject);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}