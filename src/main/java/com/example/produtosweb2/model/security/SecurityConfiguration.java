package com.example.produtosweb2.model.security;


import com.example.produtosweb2.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http            .authorizeHttpRequests(auth -> auth
                        // 1. RESTRICÕES (O que é bloqueado)
                        .requestMatchers("/produtos/**").hasRole("ADMIN")

                        // 2. LIBERAÇÕES PÚBLICAS (Visitantes podem ver)
                        .requestMatchers("/", "/login", "/error").permitAll()
                        .requestMatchers("/pessoafisica/**", "/pessoajuridica/**").permitAll() // Libera Form e Save de ambos
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll() // Importante para o layout

                        // 3.(Vendas, Listas, etc) exige login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Sua página HTML
                        .loginProcessingUrl("/login") // Para onde o POST vai (tem que bater com o HTML)
                        .defaultSuccessUrl("/vendas/list", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }

//        @Bean
//        public InMemoryUserDetailsManager userDetailsService() {
//            UserDetails user1 = User.withUsername("user")
//                    .password(passwordEncoder().encode("123"))
//                    .roles("USER")
//                    .build();
//            UserDetails admin = User.withUsername("admin")
//                    .password(passwordEncoder().encode("admin"))
//                    .roles("ADMIN")
//                    .build();
//            return new InMemoryUserDetailsManager(user1, admin);
//        }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}