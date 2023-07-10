package com.ooqn.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
@ServletComponentScan
public class ChatApplication {

    @Bean
    ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
