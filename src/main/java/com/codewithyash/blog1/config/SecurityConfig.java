package com.codewithyash.blog1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.codewithyash.blog1.security.CustomUserDetailService;
import com.codewithyash.blog1.security.JwtAuthenticationEntryPoint;
import com.codewithyash.blog1.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig {
    
	@Autowired
    private final CustomUserDetailService customUserDetailService;
    
	@Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	

    public SecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
    						// 1st
    	
    	
//        http
//        .csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
//        		.requestMatchers("/api/v1/auth/**").permitAll()
//        		.requestMatchers("/v3/api-docs").permitAll().requestMatchers(HttpMethod.GET).permitAll()
//        		.anyRequest().authenticated()
//        )
//        .exceptionHandling(ex -> ex 
//        		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
//        )
//        .sessionManagement(session -> session 
//        		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        )
//        
//        
//        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
        
        							// 3rd

//    	    http
//    	        .csrf(csrf -> csrf.disable())
//    	        .authorizeHttpRequests(auth -> auth
//    	            .requestMatchers("/api/v1/auth/**").permitAll()   // ✅ Allow Public Auth APIs
//    	            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()  // ✅ Allow Swagger
//    	            .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()  // ✅ Example: Allow GET for Public APIs
//    	            .anyRequest().authenticated()  // 🔒 Other requests need authentication
//    	        )
//    	        .exceptionHandling(ex -> ex
//    	            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//    	        )
//    	        .sessionManagement(session -> session
//    	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//    	        )
//    	        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//    	    return http.build();
    	
    	
    	    
    								//2nd
    	    
        http
        .csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers("/api/v1/auth/**").permitAll()
        .requestMatchers("/v3/api-docs").permitAll()
        .requestMatchers(HttpMethod.GET).permitAll()
        .anyRequest()
        	.authenticated().and().exceptionHandling()
        		.authenticationEntryPoint(this.jwtAuthenticationEntryPoint).and()
        
        .sessionManagement(session -> session 
        		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        
        
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
        
        
 
        
        
        
        
        
    }
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
