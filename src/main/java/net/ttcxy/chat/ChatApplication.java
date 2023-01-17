package net.ttcxy.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
@ServletComponentScan
public class ChatApplication {

    public static String host;

    @Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

    @Value("${host}")
    public void setHost(String host){
        ChatApplication.host = host;
    }
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
