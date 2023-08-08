package com.assignment.security;

import com.assignment.security.oauth2.CustomerOAuth2UserService;
import com.assignment.security.oauth2.OAuth2LoginSuccessHandler;
import com.assignment.security.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailService userDetailService() {
        return new UserDetailService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/logout", "/register/**", "/forgot-password/**", "/reset_password",
                                "/verification_account", "/sell/**", "/cat").permitAll()
                                .requestMatchers("/add-to-cart/delete-product/**",
                                        "/add-to-cart/view-cart", "/add-to-cart/buy-now").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers("/products/**", "/statistics/**", "/promotion/**", "/show-invoice").hasAuthority("ADMIN")
                                .anyRequest().authenticated()

                );
        http.formLogin()
                .loginPage("/login")
                .successHandler(new CustomAuthenticationSuccessHandler())
                .defaultSuccessUrl("/cat")
                .usernameParameter("email")
                .permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(auth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)
                .and().logout().permitAll()
                .and().rememberMe().key("abcDefgHijKlmnOpqrs_1234567890")
                .tokenValiditySeconds(7 * 24 * 60 * 60);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/images/**", "/js/**", "/product-images/**");
    }

    @Autowired
    private CustomerOAuth2UserService auth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
}
