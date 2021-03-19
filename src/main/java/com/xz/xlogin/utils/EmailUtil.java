package com.xz.xlogin.utils;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author: xz
 * @Date: 2021/3/20
 * Java Mail 相关邮箱操作工具类
 */
public class EmailUtil {

    public static void sendVerifyCode(String code, String email, JavaMailSender javaMailSender) throws MessagingException {

        //SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //simpleMailMessage.setFrom("c1076409897@126.com");
        //simpleMailMessage.setTo(email);
        //simpleMailMessage.setSubject("标题");
        //simpleMailMessage.setText("内容");
        //javaMailSender.send(simpleMailMessage);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setSubject("XLogin用户注册");
        messageHelper.setFrom("c1076409897@126.com");
        messageHelper.setTo(email);
        messageHelper.setText("内容测试内容测试:code:" + code, true);
        javaMailSender.send(mimeMessage);
    }
}
