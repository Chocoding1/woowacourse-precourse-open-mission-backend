package bogus.ai_chatbot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AI 챗봇 API 문서",
                description = "AI 챗봇 서비스 : 프론트와 협업을 위한 API 문서입니다.",
                version = "v1.0.0",
                contact = @Contact(name = "조성진", email = "galmeagi2@naver.com")
        )
)
public class OpenApiConfig {
}
