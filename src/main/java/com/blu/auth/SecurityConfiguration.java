package com.blu.auth;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /*
        Allows auth endpoints to be hit without needing a JWT token
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests()
                .requestMatchers("/**").permitAll().anyRequest().authenticated();
//                .requestMatchers("/auth/**","/paths/**","/greetings/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
        Addition cors config. Probably redundant
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {


        CorsConfiguration configuration = new CorsConfiguration();


        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));


        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));


        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));


        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration("/**", configuration);


        return source;
    }
}