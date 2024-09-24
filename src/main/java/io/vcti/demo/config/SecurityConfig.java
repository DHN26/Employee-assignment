package io.vcti.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.vcti.demo.filter.JWTFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTFilter jwtFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.csrf(csrf->csrf.disable());
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/emp/save").hasRole("ADMIN")
                .requestMatchers("/emp/fetch", "/emp/fetch/{id}", "/emp/login").permitAll()
                .requestMatchers("/emp/update/{id}", "/emp/delete/{id}").hasRole("ADMIN")
                .anyRequest().authenticated());
		http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider(encoder());
		provider.setPasswordEncoder(encoder());
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
	}
	
	BCryptPasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}
	
}
