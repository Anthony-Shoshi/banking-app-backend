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

public class
WebSecurityConfiguration {


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
                        requests.requestMatchers("...").permitAll());

        http.addFilterBefore(jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
