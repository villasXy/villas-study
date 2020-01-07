package com.villas.controller;

import com.villas.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
public class MailController {

    @Autowired
    private MailService mailService;

    @PutMapping("{to}")
    public ResponseEntity sendText(@PathVariable String[] to) {
        mailService.sendSimpleMail(to, "first email", "hello, sir");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/html/{to}")
    public ResponseEntity sendHtml(@PathVariable String[] to) {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("title", "replace: title");
        valueMap.put("content", "replace: content");

        Map<String, String> imgs = new HashMap<>();
        imgs.put("logo", "timg.jpg");

        mailService.sendHtmlMail(to, "thymeleaf", "test.html",
                (context) -> context.setVariables(valueMap),
                (helper) -> imgs.forEach((k, v) -> mailService.renderImg(helper, k, "mailTemplate/" + v))
        );
        return ResponseEntity.ok().build();
    }

    @PutMapping("/attachment/{to}")
    public ResponseEntity sendAttachments(@PathVariable String[] to) {
        String filePath = "mailTemplate/timg.jpg";

        mailService.sendHtmlMail(to, "带附件的email", "hello",
                (helper -> mailService.addAttachment(helper, filePath)));
        return ResponseEntity.ok().build();
    }


}
