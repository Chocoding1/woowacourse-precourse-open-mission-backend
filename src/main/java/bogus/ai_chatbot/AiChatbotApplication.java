package bogus.ai_chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AiChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiChatbotApplication.class, args);
	}

}
