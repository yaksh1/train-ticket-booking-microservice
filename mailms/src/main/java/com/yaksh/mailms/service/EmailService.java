package com.yaksh.mailms.service;

import com.yaksh.mailms.DTO.ResponseDataDTO;
import com.yaksh.mailms.DTO.TicketRequestDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    // Injects the JavaMailSender dependency to handle email sending
    @Autowired 
    private JavaMailSender javaMailSender;

    /**
     * Sends an email with the given recipient, subject, and message content.
     * 
     * @param to      The recipient's email address.
     * @param subject The subject of the email.
     * @param msg     The content of the email in HTML format.
     * @throws MessagingException if there is an error while sending the email.
     */
    private void sendEmailVerification(String to, String subject, String msg) throws MessagingException {
        // Create a MIME message for the email
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // Helper to set email properties such as recipient, subject, and content
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to); // Set the recipient's email address
        mimeMessageHelper.setSubject(subject); // Set the subject of the email
        mimeMessageHelper.setText(msg, true); // Set the email content (HTML)

        // Send the email
        javaMailSender.send(mimeMessage);
    }

    /**
     * Sends a verification email containing ticket booking details to the user.
     * 
     * @param ticketRequestDTO The ticket details provided by the user.
     * @param email            The recipient's email address.
     * @return ResponseDataDTO indicating the success or failure of the email operation.
     */
    public ResponseDataDTO sendVerificationEmail(TicketRequestDTO ticketRequestDTO, String email) {
        // Subject of the verification email
        String subject = "Your Train Ticket Booking Details";

        // Formatter for date and time display in the email
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");

        // Constructing the email content in HTML format
        String htmlMessage = "<html><body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;\">"
                + "<div style=\"max-width: 600px; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); margin: auto;\">"
                + "<h2 style=\"color: #007bff; text-align: center;\">Train Ticket Booking Details</h2>"
                + "<p style=\"font-size: 16px; color: #333;\">Hello,</p>"
                + "<p style=\"font-size: 16px; color: #333;\">Your ticket for travelling is confirmed. Ticket details are mentioned below:</p>"
                + "<ul style=\"font-size: 16px; color: #555;\">"
                + "<li><strong>Train ID:</strong> " + ticketRequestDTO.getTrainId() + "</li>"
                + "<li><strong>Date of Travel:</strong> " + ticketRequestDTO.getDateOfTravel() + "</li>"
                + "<li><strong>Source:</strong> " + ticketRequestDTO.getSource() + " (Arrival: " 
                + ticketRequestDTO.getArrivalTimeAtSource().format(formatter) + ")</li>"
                + "<li><strong>Destination:</strong> " + ticketRequestDTO.getDestination() + " (Arrival: " 
                + ticketRequestDTO.getReachingTimeAtDestination().format(formatter) + ")</li>"
                + "<li><strong>Booked Seats:</strong> " + ticketRequestDTO.getBookedSeatsIndex() + "</li>"
                + "</ul>"
                + "<p style=\"font-size: 16px; color: #333;\">If you did not request this, please ignore this email.</p>"
                + "<p style=\"font-size: 16px; text-align: center;\"><strong>Thank you for using our service!</strong></p>"
                + "</div></body></html>";

        try {
            // Attempt to send the email
            sendEmailVerification(email, subject, htmlMessage);
        } catch (MessagingException e) {
            // Log the error and return a failure response
            e.printStackTrace();
            return new ResponseDataDTO(false, "Failed to send verification email");
        }

        // Return a success response if the email was sent successfully
        return new ResponseDataDTO(true, "Verification email sent successfully");
    }
}