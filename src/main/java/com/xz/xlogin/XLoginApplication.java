package com.xz.xlogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class XLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(XLoginApplication.class, args);
    }

}
