package com.example.demo.web.login;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationFailureHandler customFailureHandler;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((request)->
                        request.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                .requestMatchers("/", "/members/add","/members/findMember/**", "/login", "/logout", "/css/**", "/*.ico",
                                        "/error", "/api/**","/members/js/findAddress.js", "/mailCheck").permitAll()
                                .anyRequest().authenticated())
                .formLogin(login -> login
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .usernameParameter("loginId")
                                .passwordParameter("password")
//                        .successHandler(new LoginSuccessHandler())
                                .failureHandler(customFailureHandler)
                                .permitAll()
                )
                .logout(withDefaults())
                .build();
    }


}
