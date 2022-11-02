package net.ttcxy.chat;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootApplication
@ServletComponentScan
public class ChatApplication {

    @Bean
	@Autowired
	public JPAQueryFactory jpaQuery(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}

    @Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
