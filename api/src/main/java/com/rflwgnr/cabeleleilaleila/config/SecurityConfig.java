package com.rflwgnr.cabeleleilaleila.config;

import com.rflwgnr.cabeleleilaleila.security.jwt.JwtTokenFilter;
import com.rflwgnr.cabeleleilaleila.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private final JwtTokenProvider tokenProvider;

    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(
                                        "/auth/signin",
                                        "/auth/refresh/**"
                                ).permitAll()

                                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/permissions").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/servicos", "/servicos/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/servicos").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/servicos/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/servicos/{id}").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/horarios").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/horarios/{id}").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/agendamentos").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/agendamentos/{id}").authenticated()
                                .requestMatchers(HttpMethod.POST, "/agendamentos").authenticated()
                                .requestMatchers(HttpMethod.POST, "/agendamentos/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/agendamentos/{usuarioId}/{agendamentoId}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/agendamentos/status").authenticated()

                                .requestMatchers("/users").denyAll()
                )
                .cors( cors -> {} )
                .build();
    }
}
