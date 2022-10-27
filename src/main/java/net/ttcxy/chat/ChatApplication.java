package net.ttcxy.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import cn.hutool.crypto.digest.BCrypt;

@SpringBootApplication
@ServletComponentScan
public class ChatApplication {

    public static void main(String[] args) {
        BCrypt.hashpw("123456");

        SpringApplication.run(ChatApplication.class, args);
    }

}
