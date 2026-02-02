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

@Configuration //classe de configuração
@EnableWebSecurity //indica ao Spring que serão definidas configurações personalizadas de segurança
public class SecurityConfiguration {

    @Autowired
    private UsuarioService usuarioService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(
                            customizer ->
                                    customizer
                            // Dentro do securityFilterChain
                                    .requestMatchers("/produtos/**").hasRole("ADMIN") // Bloqueia qualquer URL que comece com /produtos para quem não é ADM
                                    .requestMatchers("/vendas/**", "/pessoas/**").permitAll() // Ou .authenticated() se quiser forçar login antes de entrar
                                    .requestMatchers("/", "/login", "/error").permitAll()
                                    // Liberamos a lista para o visitante ver o aviso e os botões de cadastro
                                    .requestMatchers("/pessoas/list").permitAll()
                                    // Liberamos o formulário de venda (Catálogo)
                                    .requestMatchers("/vendas/form").permitAll()
                                    // Liberamos o acesso visual aos formulários de cadastro
                                    .requestMatchers("/pessoafisica/form", "/pessoajuridica/form").permitAll()
                                    // Liberamos o envio dos dados de cadastro (POST)
                                    .requestMatchers(HttpMethod.POST, "/pessoafisica/save", "/pessoajuridica/save").permitAll()
                                    // O resto exige login
                                    .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage("/login")
                            .defaultSuccessUrl("/vendas/list", true)
                            .permitAll()
                    )
                    .logout(logout -> logout.permitAll())
                    .userDetailsService(usuarioService);
            return http.build();
        }

        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
            UserDetails user1 = User.withUsername("user")
                    .password(passwordEncoder().encode("123"))
                    .roles("USER")
                    .build();
            UserDetails admin = User.withUsername("admin")
                    .password(passwordEncoder().encode("admin"))
                    .roles("ADMIN")
                    .build();
            return new InMemoryUserDetailsManager(user1, admin);
        }

    /**
     * Com o mét0do, instanciamos uma instância do encoder BCrypt e deixando o controle dessa instância como responsabilidade do Spring.
     * Agora, sempre que o Spring Security necessitar condificar um senha, ele já terá o que precisa configurado.
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}