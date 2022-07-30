package com.example.demo.security.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.security.filters.AuthenticationFilter;
import com.example.demo.security.filters.AuthorizationFilter;
import com.example.demo.usuario.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private final UsuarioService userDetailsService;
    private final ObjectMapper objectMapper;

    public SecurityConfig(UsuarioService userDetailsService, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated().and().csrf().disable();
        
        final String[] antPatterns = {"/usuario/**", "/login"};

        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and();

        // Set unauthorized requests exception handler
        http = http
            .exceptionHandling()
            .authenticationEntryPoint(
                (request, response, ex) -> {
                    response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        ex.getMessage()
                    );
                }
            )
            .and();
        
        // public endpoints permissions
        http.authorizeHttpRequests().antMatchers(antPatterns).permitAll().anyRequest().authenticated();

        // Set jwtTokenFilter to chain before UsernamePasswordAuthenticationFilter class
        // http.addFilterBefore(this.jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http
            .addFilter(new AuthenticationFilter(authenticationManager(), this.objectMapper))
            .addFilter(new AuthorizationFilter(authenticationManager(), this.userDetailsService));
                

        // http
        //     .cors()
        //     .and()
        //     .authorizeRequests()
        //     .antMatchers(pathMatchers).permitAll()
        //     .anyRequest().authenticated()
        //     .and()
        //     .csrf().disable()
        //     .addFilter(new AuthenticationFilter(authenticationManager()))
        //     .addFilter(new AuthorizationFilter(authenticationManager()))
        //     // disabilita a criação de sessão do spring security
        //     .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * Aqui é usado o userDetailsService do auth pra recuperar o username e usamos
         * nossa implementação "userDetailsService.loadUserByUsername" pra recuperar os 
         * dados do usuário por username.
         */
        auth.userDetailsService(
            username -> {
                System.out.println("SecurityConfig - Buscando dados do usuário");
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                return userDetails;
            }
        );
        // auth
        //     .userDetailsService(userDetailsService)
        //     .passwordEncoder(bCryptPasswordEncoder);
    }

    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    //     final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
    //     CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
    //     source.registerCorsConfiguration("/**", corsConfiguration);
    //     return source;
    // }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
