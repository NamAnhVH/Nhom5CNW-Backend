package com.WebLearning.WebLearning.Email;

import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

@Service
public class EmailVerificationService {

    @Autowired
    private AccountRepository accountRepository;

    public static final String path = "http://localhost:8080/register/verify";

    public Properties properties(){
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 587);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        return prop;
    }

    public Session session(){
        Session session = Session.getInstance(properties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("namanh20022002@gmail.com", "vmqiukchglyvgpuy");
                    }
                });
        return session;
    }

    public void sendVerificationEmailByUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        try {
            Message message = new MimeMessage(session());
            message.setFrom(new InternetAddress("Webhoctructuyen.vn", "WEB học trực tuyến"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(account.getEmail())
            );
            message.setSubject("Xác minh tài khoản");
            String verificationCode = generateVerificationCode();
            account.setVerificationCode(verificationCode);
            accountRepository.save(account);
            String content = "Xin chào, để hoàn tất đăng ký, vui lòng nhấp vào liên kết sau: " + path + "?code=" + verificationCode;
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendVerificationEmailByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        try {
            Message message = new MimeMessage(session());
            message.setFrom(new InternetAddress("Webhoctructuyen.vn", "WEB học trực tuyến"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(account.getEmail())
            );
            message.setSubject("Xác minh tài khoản");
            String verificationCode = generateVerificationCode();
            account.setVerificationCode(verificationCode);
            accountRepository.save(account);
            String content = "Xin chào, để hoàn tất đăng ký, vui lòng nhấp vào liên kết sau: " + path + "?code=" + verificationCode;
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifyEmail(String verificationCode) {
        // Tìm người dùng dựa trên mã xác minh
        Account account = accountRepository.findByVerificationCode(verificationCode);

        if (account != null) {
            // Xác minh thành công, cập nhật trạng thái xác minh và lưu vào cơ sở dữ liệu
            if(!account.isVerified()){
                account.setVerified(true);
                accountRepository.save(account);
                return true;
            }
            return false;
        } else {
            // Mã xác minh không hợp lệ
            throw new IllegalArgumentException("Mã xác minh không hợp lệ");
        }
    }

    private String generateVerificationCode() {
        // Logic để tạo mã xác minh ngẫu nhiên, ví dụ: UUID.randomUUID().toString()
        String code = UUID.randomUUID().toString();
        return code;
    }


}

