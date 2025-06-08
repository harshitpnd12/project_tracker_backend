// package com.projectmanagement.config;

// import java.util.Arrays;
// import java.util.Collections;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;

// import jakarta.servlet.http.HttpServletRequest;

// @Configuration
// @EnableWebSecurity
// public class AppConfig {

//     @Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .authorizeHttpRequests(authorize -> authorize
//                         .requestMatchers("/api/**").authenticated()
//                         .anyRequest().permitAll())
//                 .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
//                 .csrf(csrf -> csrf.disable())
//                 .cors(cors -> cors.configurationSource(corsConfigurationSource()));

//         return http.build();
//     }

//     private CorsConfigurationSource corsConfigurationSource() {
//         return new CorsConfigurationSource() {

//             @Override
//             public CorsConfiguration getCorsConfiguration(@SuppressWarnings("null") HttpServletRequest request) {
//                 CorsConfiguration cfg = new CorsConfiguration();
//                 cfg.setAllowedOrigins(Arrays.asList(
//                         "http://localhost:3000",
//                         "https://project-tracker-frontend-three.vercel.app",
//                         "http://localhost:5173",
//                         "http://localhost:4200"));
//                 cfg.setAllowedMethods(Collections.singletonList("*"));
//                 cfg.setAllowCredentials(true);
//                 cfg.setAllowedHeaders(Collections.singletonList("*"));
//                 cfg.setExposedHeaders(Arrays.asList("Authorization"));
//                 cfg.setMaxAge(3600L);
//                 return cfg;
//             }
//         };
//     }

//     @Bean
//     PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }

package com.projectmanagement.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Stateless session (no session maintained)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Allow all OPTIONS requests for CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Secure /api/** endpoints
                        .requestMatchers("/api/**").authenticated()

                        // Allow all other requests without authentication
                        .anyRequest().permitAll())

                // Add your JWT validator filter before BasicAuthenticationFilter
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)

                // Disable CSRF for APIs
                .csrf(csrf -> csrf.disable())

                // Enable CORS with custom config source
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();

                // Allowed origins (no trailing slash)
                cfg.setAllowedOriginPatterns(Arrays.asList(
                        "http://localhost:3000",
                        "https://project-tracker-frontend-three.vercel.app",
                        "http://localhost:5173",
                        "http://localhost:4200"));

                cfg.setAllowedOrigins(Arrays.asList(
                        "http://localhost:3000",
                        "https://project-tracker-frontend-three.vercel.app"));

                // Allowed HTTP methods explicitly listed
                cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                // Allow credentials (cookies, authorization headers)
                cfg.setAllowCredentials(true);

                // Allow all headers
                cfg.setAllowedHeaders(Collections.singletonList("*"));

                // Expose Authorization header to the client
                cfg.setExposedHeaders(Arrays.asList("Authorization"));

                // Cache preflight response for 1 hour
                cfg.setMaxAge(3600L);

                return cfg;
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
