package util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil
{
    public static Session getMailSession() 
    {
        String host     = "smtp.gmail.com";			// Gmail SMTP 서버 주소
        String sender   = "djang7620@gmail.com";	// 메일을 발송할 Gmail 계정
        String password = "hoeyzfftzqkjchjo";		// Gmail 앱 비밀번호

        Properties props = new Properties();			// SMTP 서버에 필요한 설정값 저장

        props.put("mail.smtp.host", host);				// SMTP 서버 주소
        props.put("mail.smtp.port", "587");				// Gmail SMTP 포트 587번 포트는 STARTTLS 방식 사용
        props.put("mail.smtp.auth", "true");			// SMTP 인증 사용
        
        //암호화 O
        //Java → 🔒 암호화된 통신 → Gmail
        //암호화 X
        //Java → 이메일 내용 → Gmail
        
        props.put("mail.smtp.starttls.enable", "true");	// TLS 암호화 사용

        // SMTP 설정과 인증 정보를 이용하여 메일 세션 생성
        Session session = Session.getInstance
        		(
		            props,
		            new Authenticator() 
		            {
		                @Override
		                protected PasswordAuthentication getPasswordAuthentication() 
		                {
		                    return new PasswordAuthentication(sender,password);
		                }
		            }
		        );
        return session;
    }


    public static void sendEmail(String to, String code) throws Exception {

        Session session = getMailSession();

        Message message = new MimeMessage(session);

        message.setFrom(
            new InternetAddress("djang7620@gmail.com")
        );

        message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(to)
        );

        message.setSubject("MyFlix 이메일 인증번호");

        message.setText(
            "안녕하세요. MyFlix입니다.\n\n"
            + "이메일 인증번호는 [" + code  + "] 입니다.\n\n"
            + "인증번호를 입력하여 이메일 인증을 완료해주세요."
        );

        Transport.send(message);
    }
    
    public static void sendId(String to, String userid) throws Exception {

        Session session = getMailSession();

        Message message = new MimeMessage(session);

        message.setFrom(
            new InternetAddress("djang7620@gmail.com")
        );

        message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(to)
        );

        message.setSubject("MyFlix 계정 ID");

        message.setText(
            "안녕하세요. MyFlix입니다.\n\n"
            + "계정 ID는 [" + userid  + "] 입니다."
        );

        Transport.send(message);
    }
    
    public static void sendPw(String to, String userpw) throws Exception {

        Session session = getMailSession();

        Message message = new MimeMessage(session);

        message.setFrom(
            new InternetAddress("djang7620@gmail.com")
        );

        message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(to)
        );

        message.setSubject("MyFlix 재발급 PW");

        message.setText(
            "안녕하세요. MyFlix입니다.\n\n"
            + "계정 재발급 PW는 [" + userpw  + "] 입니다."
        );

        Transport.send(message);
    }
}