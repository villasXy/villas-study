package com.example.controller;

import com.example.kafka.KfkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaTestController {

    @Autowired
    private KfkaProducer producer;

    @RequestMapping("/testSendMsg")
    public String testSendMsg(){
        producer.send();
        return "success";
    }
}
