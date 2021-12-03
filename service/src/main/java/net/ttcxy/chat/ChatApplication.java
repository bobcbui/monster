package net.ttcxy.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"net.ttcxy.chat"})
@MapperScan(basePackages ={"net.ttcxy.chat.mapper"})
@MapperScan(basePackages = {"net.ttcxy.chat.dao"})
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
