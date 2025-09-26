package com.example.SpringWebSocket.Configuration;


import com.example.SpringWebSocket.Services.UserDetailsImplmentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsImplmentation userDetailsImplmentation;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsImplmentation);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                //.authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers("/","/newuser","/ws/**").permitAll();
                    auth.requestMatchers("/home").authenticated();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/first?error=true")
                        .usernameParameter("id")
                        .passwordParameter("password")
                );
//                .rememberMe(remember -> remember
//                        //.key("uniqueAndSecret") // Secret key to sign token
//                        .tokenValiditySeconds(60 * 60) // 1 day
//                        .rememberMeParameter("remember-me") // name of the checkbox
//                        .userDetailsService(userDetailsImplmentation)
//                );
        return http.build();
    }
}
