package com.college.club.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.college.club.management.repositories.UserRepository;



@Configuration
public class ApplicationConfig {
	
	UserRepository userRepository;

    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    @Bean
    UserDetailsService userDetailsService() {
    	return username-> userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
    }
    
    @Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
    @Bean
     AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    AuthenticationProvider authenticationProvider() {
    	
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    	
    	authProvider.setUserDetailsService(userDetailsService());
    	authProvider.setPasswordEncoder(encoder());
    	
    	return authProvider;
    }

    

}
