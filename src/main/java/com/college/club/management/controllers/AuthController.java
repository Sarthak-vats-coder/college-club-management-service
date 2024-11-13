package com.college.club.management.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

import com.college.club.management.entities.Club;
import com.college.club.management.entities.User;
import com.college.club.management.exception.UserNotFound;
import com.college.club.management.models.SignInRequest;
import com.college.club.management.response.AuthResponse;
import com.college.club.management.services.AuthServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RestControllerAdvice
@RequestMapping("/auth")
public class AuthController {
	AuthServices authServices;

	public AuthController(AuthServices authServices) {
		this.authServices = authServices;
	}
	
	@PostMapping(value = "/createUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<User> createUser(@RequestParam("user") String userJson,
	        @RequestParam("profilePicture") MultipartFile profilePicture) throws Exception{
		User user = new ObjectMapper().readValue(userJson, User.class);
		return ResponseEntity.ok(authServices.createUser(user, profilePicture));
		
	}
	
	@PostMapping("/signIn")
	public ResponseEntity<ResponseEntity<User>> signIn(@RequestBody SignInRequest signInRequest,HttpServletResponse response) throws UserNotFound{
		return ResponseEntity.ok(authServices.signIn(signInRequest, response));
		
	}
	
	@GetMapping("/logOut")
	public ResponseEntity<ResponseEntity<AuthResponse>> logOut(HttpServletResponse response) {
		return ResponseEntity.ok(authServices.logOut(response));
	}
	
	

}
