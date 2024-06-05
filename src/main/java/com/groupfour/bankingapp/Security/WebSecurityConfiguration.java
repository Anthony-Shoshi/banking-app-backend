package com.groupfour.bankingapp.Security;

import com.groupfour.bankingapp.Filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

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
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/employees/customer-accounts").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/transactions").permitAll()
                        .requestMatchers("/employees/customers-without-accounts").permitAll()
                        .requestMatchers("/employees/customers-without-accounts/{userId}/approve-signup").permitAll()
                        .requestMatchers("/customers/{customerId}/transactions").permitAll()
                        .requestMatchers("/customers/transaction-history").permitAll()
                        .requestMatchers("/employees/update-daily-limit").permitAll()
                        .requestMatchers("/account-detail").permitAll()
                        .requestMatchers("/register/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
