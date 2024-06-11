//package com.groupfour.bankingapp.Security;
//
//
//import com.groupfour.bankingapp.Filter.JwtFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//
//public class
//WebSecurityConfiguration {
//
//
//    private final JwtFilter jwtFilter;
//
//    public WebSecurityConfiguration(JwtFilter jwtFilter) {
//        this.jwtFilter = jwtFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable);
//
//        http.sessionManagement(
//                session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("/login").permitAll());
//    http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("/employees/customer-accounts").permitAll());
//        http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("/h2-console").permitAll());
//        http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("/transactions").permitAll());
//        http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("/employees/customers-without-accounts").permitAll());
//        http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("/employees/customers-without-accounts/{userId}/approve-signup").permitAll());
//        http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("/customers/{customerId}/transactions").permitAll());
//        http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("/customers/transaction-history").permitAll());
//        http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("/customers/search-iban").permitAll());
//        http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("/customers/deposit").permitAll());
//        http.authorizeHttpRequests(
//                requests ->
//                        requests.requestMatchers("...").permitAll());
//
//        http.addFilterBefore(jwtFilter,
//                UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}


package com.groupfour.bankingapp.Security;

import com.groupfour.bankingapp.Filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final JwtFilter jwtFilter;

    public WebSecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://anthony-shoshi.github.io/banking-app-frontend", "https://anthony-shoshi.github.io"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/employees/customer-accounts").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/transactions").permitAll()
                        .requestMatchers("/employees/customers-without-accounts").permitAll()
                        .requestMatchers("/employees/customers-without-accounts/{userId}/approve-signup").permitAll()
                        .requestMatchers("/employees/close-account/{customerId}").permitAll()
                        .requestMatchers("/customers/{customerId}/transactions").permitAll()
                        .requestMatchers("/customers/transaction-history").permitAll()
                        .requestMatchers("/employees/update-daily-limit").permitAll()
                        .requestMatchers("/account-detail").permitAll()
                        .requestMatchers("/customers/search-iban").permitAll()
                        .requestMatchers("/customers/deposit").permitAll()
                        .requestMatchers("/customers/withdraw").permitAll()
                        .requestMatchers("/accounts/{userId}").permitAll()
                        .requestMatchers("/register/**").permitAll()
                        .anyRequest().authenticated()
                );


//        http.sessionManagement(
//                session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
//                cors(cors -> cors.configurationSource(corsConfigurationSource()));
//        http.authorizeHttpRequests(
//                authorize ->authorize
//                        .requestMatchers("/login").permitAll()
//                        .requestMatchers("/employees/customer-accounts").permitAll()
//                        .requestMatchers("/h2-console/**").permitAll()
//                        .requestMatchers("/transactions").permitAll()
//                        .requestMatchers("/employees/customers-without-accounts").permitAll()
//                        .requestMatchers("/employees/customers-without-accounts/{userId}/approve-signup").permitAll()
//                        .requestMatchers("/customers/{customerId}/transactions").permitAll()
//                        .requestMatchers("/account-detail").permitAll()
//                        .requestMatchers("/register/**").permitAll()
//                        .anyRequest().authenticated());
//
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
       // http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
