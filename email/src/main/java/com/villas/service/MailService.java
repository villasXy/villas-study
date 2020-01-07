package com.villas.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.function.Consumer;

/**
 * 发邮件的接口
 *
 * @author villas
 */
public interface MailService {
    /**
     * 发送文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String[] to, String subject, String content);

    /**
     * 发送html邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     * @param operate 可以添加附件，或者在html中添加图片等操作
     */
    void sendHtmlMail(String[] to, String subject, String content, Consumer<MimeMessageHelper> operate);

    /**
     * 发送html邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param html    html文件名
     * @param render  装配thymeleaf的非图片等数据
     * @param operate 可以添加附件，或者在html中添加图片等操作
     */
    void sendHtmlMail(String[] to, String subject, String html, Consumer<Context> render,
                      Consumer<MimeMessageHelper> operate);


    /**
     * 添加类路径中附件
     *
     * @param helper   helper
     * @param filePath 类路径
     */
    default void addAttachment(MimeMessageHelper helper, String filePath) {
        try {
            InputStreamSource resource = new ClassPathResource(filePath);
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            helper.addAttachment(fileName, resource);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片写入邮件页面中
     *
     * @param helper helper
     * @param key 替换文件的字符串key
     * @param path 文件路径
     */
    default void renderImg(MimeMessageHelper helper, String key, String path) {
        try {
            helper.addInline(key, new ClassPathResource(path));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
