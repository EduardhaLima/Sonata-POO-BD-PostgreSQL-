package com.sonata.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracao MINIMA de seguranca para está primeira entrega da API.
 *
 * <p>Por enquanto:
 * <ul>
 *   <li>desabilita CSRF (API stateless);</li>
 *   <li>libera todos os endpoints (autenticacao real - JWT - virá depois);</li>
 *   <li>expoe um {@link BCryptPasswordEncoder} para hash de senha.</li>
 * </ul>
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    /** Algoritmo recomendado para hash de senha (BCrypt). */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
