package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Repository.AccountRepository;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Repository.TeacherProfileRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
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
public class EmailService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherProfileRepository teacherProfileRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    public static final String path = "http://localhost:8080/";

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
            String content = "Xin chào, để hoàn tất đăng ký, vui lòng nhấp vào liên kết sau: " + path + "register/verify?code=" + verificationCode;
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
            String content = "Xin chào, để hoàn tất đăng ký, vui lòng nhấp vào liên kết sau: " + path + "register/verify?code=" + verificationCode;
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifyEmail(String verificationCode) {
        Account account = accountRepository.findByVerificationCode(verificationCode);
        if (account != null) {
            if(!account.isVerified()){
                account.setVerified(true);
                accountRepository.save(account);
                return true;
            }
            return false;
        } else {
            throw new IllegalArgumentException("Mã xác minh không hợp lệ");
        }
    }

    private String generateVerificationCode() {
        String code = UUID.randomUUID().toString();
        return code;
    }

    public void sendNoticeTo(Long id, String mainContent) {
        Account account = accountRepository.findById(id).get();
        try {
            Message message = new MimeMessage(session());
            message.setFrom(new InternetAddress("Webhoctructuyen.vn", "WEB học trực tuyến"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(account.getEmail())
            );
            String content = null;
            if(mainContent.equals("approveAccount")){
                message.setSubject("Tài khoản được cấp quyền");
                content = "Tài khoản của bạn đã được chấp thuận, bạn có thể bắt đầu sử dụng hệ thống ngay bây giờ." +
                        "<br>" +
                        "<br>" +
                        "Chúng tôi chân thành cảm ơn sự hợp tác của bạn.";
            }
            if(mainContent.equals("lockAccount")){
                message.setSubject("Tài khoản đã bị khoá");
                content = "Tài khoản của bạn đã bị khoá do vi phạm quy định của hệ thống, nếu có gì sai sót xin vui lòng liên hệ bên quản trị hệ thống." +
                        "<br>" +
                        "<br>" +
                        "Chúng tôi chân thành xin lỗi vì sự bất tiện này.";
            }
            if(mainContent.equals("unlockAccount")){
                message.setSubject("Tài khoản đã được mở khoá");
                content = "Tài khoản của bạn đã được mở khoá, bạn có thể bắt đầu sử dụng hệ thống ngay bây giờ." +
                        "<br>" +
                        "<br>" +
                        "Chúng tôi chân thành cảm ơn sự hợp tác của bạn.";
            }
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNoticeAboutCourse(Long id, String mainContent) {
        Course course = courseRepository.findById(id).get();
        Account account = teacherProfileRepository.findById(course.getTeacher().getId()).get().getAccount();
        try {
            Message message = new MimeMessage(session());
            message.setFrom(new InternetAddress("Webhoctructuyen.vn", "WEB học trực tuyến"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(account.getEmail())
            );
            String content = null;
            if(mainContent.equals("approveCourse")){
                message.setSubject("Khoá học được phê duyệt");
                content = "Khoá học <strong>" + course.getName() +  "<strong> của bạn đã được phê duyệt, khoá học đã có thể được truy cập trên hệ thống." +
                        "<br>" +
                        "<br>" +
                        "Chúng tôi chân thành cảm ơn sự hợp tác của bạn.";
            }
            if(mainContent.equals("lockCourse")){
                message.setSubject("Khoá học đã bị khoá");
                content = "Khoá học <strong>" + course.getName() +  "<strong> của bạn đã bị khoá do vi phạm quy định của hệ thống, khoá học tạm thời không được truy cập trên hệ thống." +
                        "<br>" +
                        "Nếu có gì sai sót, xin vui lòng liên hệ với bên quản trị hệ thống." +
                        "<br>" +
                        "<br>" +
                        "Chúng tôi xin lỗi vì sự bất tiện này.";
            }
            if(mainContent.equals("unlockCourse")){
                message.setSubject("Khoá học được mở khoá");
                content = "Khoá học <strong>" + course.getName() +  "<strong> của bạn đã mở khoá, khoá học đã có thể được truy cập trên hệ thống." +
                        "<br>" +
                        "<br>" +
                        "Chúng tôi chân thành cảm ơn sự hợp tác của bạn.";
            }
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendNoticeChangeEmailTo(String email) {
        Account account = authenticationFacade.getAccount();
        try {
            Message message = new MimeMessage(session());
            message.setFrom(new InternetAddress("Webhoctructuyen.vn", "WEB học trực tuyến"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject("Xác minh tài khoản email");
            String verificationCode = generateVerificationCode();
            account.setVerificationCode(verificationCode);
            accountRepository.save(account);
            String content = "Xin chào, để hoàn tất việc chỉnh sửa email, vui lòng nhấp vào liên kết sau: " +
                    path + "account/changeEmail/verify?code=" + verificationCode;
            message.setContent(content, "text/html; charset=utf-8");
            authenticationFacade.getAccount().setEmail(email);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendVerificationChangePasswordToEmail(String email) {
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
            String content = "Xin chào, để thay đổi mật khẩu mới, vui lòng nhấp vào liên kết sau: " + path + "login/forgetPassword/?verifySuccess&code=" + verificationCode;
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

