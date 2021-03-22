package com.xz.xlogin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xz
 * @Date: 2021/3/20
 * Java Mail 相关邮箱操作工具类
 */
@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;

    public void sendVerifyCode(String code, String email) throws MessagingException {


        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("code", code);
        context.setVariable("validityTime","5分钟");

        //读取短信码html模板
        String templateContent = templateEngine.process("emailTemplet", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("c1076409897@126.com");
        helper.setTo(email);
        helper.setSubject("XLogin用户注册");
        helper.setText(templateContent, true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                javaMailSender.send(message);
            }
        }).start();
    }
}
