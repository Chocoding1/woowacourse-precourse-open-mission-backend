package bogus.ai_chatbot.config;

import bogus.ai_chatbot.domain.security.filter.CustomLogoutFilter;
import bogus.ai_chatbot.domain.security.filter.ExceptionHandlerFilter;
import bogus.ai_chatbot.domain.security.filter.JwtAuthenticationFilter;
import bogus.ai_chatbot.domain.security.filter.LoginFilter;
import bogus.ai_chatbot.domain.jwt.util.JwtUtil;
import bogus.ai_chatbot.domain.security.properties.PermitPaths;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final PermitPaths permitPaths;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration corsConfiguration = new CorsConfiguration();

                                corsConfiguration.setAllowedOrigins(List.of("http://localhost:5500"));
                                corsConfiguration.setAllowedMethods(List.of("*"));
                                corsConfiguration.setAllowCredentials(true);
                                corsConfiguration.setAllowedHeaders(List.of("*"));
                                corsConfiguration.setMaxAge(3600L);

                                corsConfiguration.setExposedHeaders(List.of("Authorization", "Authorization-Refresh"));

                                return null;
                            }
                        }))

                .csrf(CsrfConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .logout(LogoutConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(getPermitPaths(permitPaths)).permitAll()
                        .requestMatchers("/chats/**").permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterAt(new CustomLogoutFilter(jwtUtil), LogoutFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), CustomLogoutFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthenticationFilter(jwtUtil, permitPaths), LoginFilter.class);

        return http.build();
    }

    private String[] getPermitPaths(PermitPaths permitPaths) {
        return permitPaths.getPaths().toArray(String[]::new);
    }
}
