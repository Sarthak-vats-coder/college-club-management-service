package com.college.club.management.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final AuthenticationProvider authenticationProvider;
	private final JwtValidator jwtValidator;

	public WebSecurityConfig(JwtValidator jwtTokenValidator, AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
		this.jwtValidator = jwtTokenValidator;
	}

	@Bean
	@Primary
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf -> csrf.disable()).sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/view/**").permitAll()
						.requestMatchers("/index.html").permitAll()
						.requestMatchers("/favicon.ico").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/**").permitAll()
//						.requestMatchers("/logo192.png").permitAll()
						.requestMatchers("/static/**").permitAll()
						.requestMatchers("/logs/**").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers("/admin/**").hasRole("ROOT_ADMIN")
						.requestMatchers("/club/**").hasAnyRole("ROOT_ADMIN", "CLUB_ADMIN")
						.requestMatchers("/user/**").hasAnyRole("ROOT_ADMIN", "CLUB_ADMIN", "USER")
						.requestMatchers("/swagger-ui/**").permitAll()
						.requestMatchers("/v3/api-docs/**").permitAll()
						.anyRequest().authenticated())
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtValidator, UsernamePasswordAuthenticationFilter.class)
				.cors(cors -> cors.configurationSource(corsConfigurationSource())).build();
	}

	private CorsConfigurationSource corsConfigurationSource() {
		return new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration ccfg = new CorsConfiguration();
				ccfg.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000","https://effervescent-starburst-4152d3.netlify.app/",
						"http://localhost:1025", "http://127.0.0.1:1025"));
				ccfg.setAllowedMethods(Collections.singletonList("*"));
				ccfg.setAllowCredentials(true);
				ccfg.setAllowedHeaders(Collections.singletonList("*"));
				ccfg.setMaxAge(3600L);

				return ccfg;

			}
		};

	}

}
