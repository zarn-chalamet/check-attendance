package com.ai.attendance_check.notification_service.service.impl;

import com.ai.attendance_check.notification_service.dto.AttendanceWarningMessage;
import com.ai.attendance_check.notification_service.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(AttendanceWarningMessage attendanceWarningMessage) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Set recipient
            helper.setTo(attendanceWarningMessage.getEmail());

            // Set sender
            helper.setFrom("zarnn872@gmail.com", "Attendance Check System");

            // Set subject
            helper.setSubject("⚠ Attendance Warning for " + attendanceWarningMessage.getCourseTitle());

            // Build HTML email content
            String emailContent = """
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <h2 style="color: #d9534f;">Attendance Warning</h2>
                    <p>Dear <strong>%s</strong>,</p>
                    <p>This is a notice regarding your attendance in the course <strong>%s</strong> (ID: %s).</p>
                    <p>Your current attendance rate is: <strong>%.2f%%</strong>.</p>
                    <p style="color: #d9534f;">According to our policy, students with attendance below 75%% may face academic consequences.</p>
                    <hr>
                    <p style="font-size: 0.9em; color: #555;">
                        Please make sure to attend the upcoming sessions regularly to improve your attendance.
                        If you have any concerns, contact your course instructor immediately.
                    </p>
                    <p>Best regards,<br>Attendance Check Team</p>
                </body>
                </html>
                """.formatted(
                    attendanceWarningMessage.getStudentName(),
                    attendanceWarningMessage.getCourseTitle(),
                    attendanceWarningMessage.getCourseId(),
                    attendanceWarningMessage.getAttendanceRate()
            );

            // Set content
            helper.setText(emailContent, true); // true = HTML

            // Send email
            javaMailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send attendance warning email", e);
        }
    }
}
