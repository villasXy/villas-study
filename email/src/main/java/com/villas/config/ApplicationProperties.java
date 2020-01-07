package com.villas.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final Mail mail = new Mail();

    @Data
    public static class Mail{
        private String from;
    }
}
