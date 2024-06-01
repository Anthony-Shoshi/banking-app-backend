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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final JwtFilter jwtFilter;

    public WebSecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement(
                session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/login").permitAll());
    http.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/employees/customer-accounts").permitAll());
        http.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/h2-console").permitAll());
        http.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/transactions").permitAll());
        http.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/employees/customers-without-accounts").permitAll());
        http.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/employees/customers-without-accounts/{userId}/approve-signup").permitAll());
        http.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/customers/{customerId}/transactions").permitAll());
        http.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/customers/transaction-history").permitAll());
        http.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/employees/update-daily-limit").permitAll());
        http.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("...").permitAll());

        http.authorizeHttpRequests(requests -> {
            requests.requestMatchers("/login").permitAll();
            requests.requestMatchers("/employees/customer-accounts").permitAll();
            requests.requestMatchers("/h2-console/**").permitAll();
            requests.requestMatchers("/transactions").permitAll();
            requests.requestMatchers("/employees/customers-without-accounts").permitAll();
            requests.requestMatchers("/employees/customers-without-accounts/{userId}/approve-signup").permitAll();
            requests.requestMatchers("/customers/{customerId}/transactions").permitAll();
            requests.requestMatchers("/customers/transaction-history").permitAll();
            requests.requestMatchers("/customers/search-iban").permitAll();
            requests.requestMatchers("/customers/deposit").permitAll();
            requests.requestMatchers("/customers/withdraw").permitAll();
            // Add more requestMatchers as needed
            requests.anyRequest().authenticated(); // All other requests need to be authenticated
        });

        // Disable X-Frame-Options to allow H2 console to be loaded in a frame
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
