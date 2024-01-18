package com.blog.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "gtd8983@gmail.com";
    private static String number;

    // 인증코드 생성
    public static void createNumber(){
        number = UUID.randomUUID().toString().substring(0,8);
    }

    public String sendMail(String mail){
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);
        return number;
    }

    public MimeMessage createMail(String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO,mail);
            message.setSubject("이메일 인증");
            String body = "<div style='font-family: Arial, sans-serif;'>";
            body += "<h2 style='color: orange;'>세차매니아 인증 코드</h2>";
            body += "<hr/>";
            body += "<p>안녕하세요.</p>";
            body += "<p>귀하의 이메일 주소를 통해 세차매니아에 대한 액세스가 요청되었습니다.</p>";
            body += "<p>요청하신 인증 번호를 안내해드립니다:</p>";
            body += "<h1 style='font-size: 2em; margin: 20px 0;'>" + number + "</h1>";
            body += "<p>인증 번호를 정확히 입력하여 계속 진행해주세요.</p>";
            body += "<p>감사합니다.</p>";
            body += "</div>";
            message.setText(body,"UTF-8","html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }



}
