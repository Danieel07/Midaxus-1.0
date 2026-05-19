package com.example.midaxus.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails.
 */
@Service
public class EmailService {

  private final JavaMailSender mailSender;

  /**
   * Constructs EmailService with the provided JavaMailSender.
   *
   * @param mailSender the mail sender to use
   */
  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * Sends an HTML email.
   *
   * @param to the recipient email address
   * @param subject the email subject
   * @param htmlContent the HTML content of the email
   * @throws MessagingException if an error occurs during email creation or sending
   */
  public void sendHtmlEmail(String to, String subject, String htmlContent)
      throws MessagingException {

    MimeMessage message = mailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setFrom("midaxushorarios@gmail.com");
    helper.setText(htmlContent, true); // true = HTML

    mailSender.send(message);
  }
}

