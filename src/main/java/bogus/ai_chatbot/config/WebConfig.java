package bogus.ai_chatbot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5500", "http://127.0.0.1:5500") // 브라우저의 url이 localhost가 아닌 127.0.0.1 상태이면 localhost만 잡아서는 요청이 들어오지 않음
                .allowedMethods("*");
    }
}
