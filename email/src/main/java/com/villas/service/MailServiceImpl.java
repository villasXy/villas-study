package com.villas.service;

import com.villas.config.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 邮件实现接口
 *
 * @author villas
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ApplicationProperties properties;

    @Override
    public void sendSimpleMail(String[] to, String subject, String content) {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(properties.getMail().getFrom());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(String[] to, String subject, String content, Consumer<MimeMessageHelper> operate) {
        //获取MimeMessage对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            // 这里的true代表这个消息是multipart了性的
            messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(properties.getMail().getFrom());
            messageHelper.setTo(to);
            message.setSubject(subject);
            //邮件内容，html格式
            messageHelper.setText(content, true);
            if (operate != null) {
                operate.accept(messageHelper);
            }
            //发送
            mailSender.send(message);
            log.info("邮件已经发送。");
        } catch (MessagingException e) {
            log.error("发送邮件时发生异常！", e);
        }
    }

    @Override
    public void sendHtmlMail(String[] to, String subject, String htmlFile, Consumer<Context> render,
                             Consumer<MimeMessageHelper> operate) {
        Objects.requireNonNull(render);
        Context context = new Context();
        render.accept(context);
        String content = templateEngine.process(htmlFile, context);
        sendHtmlMail(to, subject, content, operate);
    }

}
