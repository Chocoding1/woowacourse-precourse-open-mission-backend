package bogus.ai_chatbot.domain.security.properties;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.permit-list")
@Getter
//@RequiredArgsConstructor
public class PermitPaths {

    private final List<String> paths = new ArrayList<>();
}
