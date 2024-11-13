package com.college.club.management.serviceImpl;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class AuthServiceImpl implements AuthServices {
	UserRepository userRepository;
	RoleRepository roleRepository;
	PasswordEncoder passwordEncoder;
	RootAdminServices rootAdminServices;
	JwtProvider jwtProvider;

	public AuthServiceImpl(JwtProvider jwtProvider,PasswordEncoder passwordEncoder, UserRepository userRepository,RoleRepository roleRepository,RootAdminServices rootAdminServices) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.jwtProvider = jwtProvider;
		this.rootAdminServices = rootAdminServices;
	}

	@Override
	public User createUser(User user,MultipartFile profilePicture) throws Exception {
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
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setUsername(user.getUsername());
		newUser.setFirstname(user.getFirstname());
		newUser.setLastname(user.getLastname());
		newUser.setRoles(Collections.singleton(userRole));
		try {
	        if (profilePicture != null && !profilePicture.isEmpty()) {
	            newUser.setProfilePicture(profilePicture.getBytes());
	        }
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to save profile picture", e);
	    }
		System.out.print("created");
		return (userRepository.save(newUser));
	}

	@Override
	public ResponseEntity<User> signIn(SignInRequest signInRequest, HttpServletResponse response)
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
			jwtCookie.setDomain("");
			jwtCookie.setPath("/");
			jwtCookie.setMaxAge(24 * 3600);
			jwtCookie.setAttribute("SameSite", "None");
			jwtCookie.setSecure(true);
			jwtCookie.setHttpOnly(false);

			response.addCookie(jwtCookie);

			authResponse.setMessage("success");
			authResponse.setJwt(token);

			return new ResponseEntity<>(user, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@Override
	public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
		AuthResponse authResponse = new AuthResponse();

		Cookie jwtCookie = new Cookie("auth_token", "");
		jwtCookie.setDomain("");
		jwtCookie.setPath("/");
		jwtCookie.setMaxAge(0);
		jwtCookie.setAttribute("SameSite", "None");
		jwtCookie.setSecure(true);
		jwtCookie.setHttpOnly(false);

		response.addCookie(jwtCookie);

		authResponse.setMessage("success");
		System.out.println("done");
		return new ResponseEntity<>(authResponse,HttpStatus.OK);
	}

	
}
