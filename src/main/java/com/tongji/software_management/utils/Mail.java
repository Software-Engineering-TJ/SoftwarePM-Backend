package com.tongji.software_management.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component//加入到spring容器
public class Mail {

    //要想让@Value生效，必须啊使用注入的方式Autowired，而不能用new Mail()
    @Value("${spring.mail.username}")
    private String sender;

    @Resource
    JavaMailSender mailSender;

    public void sendMail(String receiver,String subject,String text)
    {
        SimpleMailMessage message = new SimpleMailMessage();//邮件对象
        message.setFrom(sender); // 邮件发送者
        message.setTo(receiver); // 邮件接受者
        message.setSubject(subject); // 主题
        message.setText(text); // 内容

        mailSender.send(message);
    }

}
