package com.college.club.management.serviceImpl;

import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.college.club.management.config.JwtProvider;
import com.college.club.management.entities.Role;
import com.college.club.management.entities.User;
import com.college.club.management.exception.UserNotFound;
import com.college.club.management.models.SignInRequest;
import com.college.club.management.repositories.RoleRepository;
import com.college.club.management.repositories.UserRepository;
import com.college.club.management.response.AuthResponse;
import com.college.club.management.services.AuthServices;
import com.college.club.management.services.RootAdminServices;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class AuthServiceImpl implements AuthServices {
	UserRepository userRepository;
	RoleRepository roleRepository;
	PasswordEncoder passwordEncoder;
	RootAdminServices rootAdminServices;
	JwtProvider jwtProvider;

	public AuthServiceImpl(JwtProvider jwtProvider,PasswordEncoder passwordEncoder, UserRepository userRepository,RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.jwtProvider = jwtProvider;
	}

	@Override
	public User createUser(@RequestBody User user) throws Exception {
		 Optional<Role> userRoleOptional = roleRepository.findByName("USER");
		    
		    Role userRole;
		    if (userRoleOptional.isEmpty()) {
		        userRole = new Role("USER");
		        userRole = roleRepository.save(userRole);  
		        System.out.println("USER role created and saved to database.");
		    } else {
		        userRole = userRoleOptional.get();
		        System.out.println("USER role found in the database.");
		    }
		String username = user.getUsername();
		Optional<User> usernameExist = userRepository.findByUsername(username);
		if (usernameExist.isPresent() && usernameExist.get().getUsername() != null) {
			throw new Exception("username Is Already Used With Another Account" + usernameExist);
		}
		
		User newUser = new User();
		newUser.setPassword(user.getPassword());
		newUser.setUsername(user.getUsername());
		newUser.setRoles(Collections.singleton(userRole));
		return (userRepository.save(newUser));
	}

	@Override
	public ResponseEntity<AuthResponse> signIn(@RequestBody  SignInRequest signInRequest, HttpServletResponse response)
			throws UserNotFound {
		String username = signInRequest.getUsername();
		User user = rootAdminServices.findUserByUsername(username);
		String userPassword = user.getPassword();
		String password = signInRequest.getPassword();

		Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		if (user != null && passwordEncoder.matches(password, userPassword)) {

			String token = jwtProvider.generateToken(signInRequest.getUsername());

			AuthResponse authResponse = new AuthResponse();

			Cookie jwtCookie = new Cookie("auth_token", token);
			jwtCookie.setDomain("localhost");
			jwtCookie.setPath("/");
			jwtCookie.setMaxAge(24 * 3600);
			jwtCookie.setAttribute("SameSite", "Lax");
			jwtCookie.setSecure(false);
			jwtCookie.setHttpOnly(false);

			response.addCookie(jwtCookie);

			authResponse.setMessage("success");
			authResponse.setJwt(token);

			return new ResponseEntity<>(authResponse, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	
}
