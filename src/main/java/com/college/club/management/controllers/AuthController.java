package com.college.club.management.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.college.club.management.entities.User;
import com.college.club.management.exception.UserNotFound;
import com.college.club.management.models.SignInRequest;
import com.college.club.management.response.AuthResponse;
import com.college.club.management.services.AuthServices;

import jakarta.servlet.http.HttpServletResponse;
@RestController
@RestControllerAdvice
@RequestMapping("/auth")
public class AuthController {
	AuthServices authServices;

	public AuthController(AuthServices authServices) {
		this.authServices = authServices;
	}
	
	@PostMapping("/createUser")
	public ResponseEntity<User> createUser(@RequestBody User user) throws Exception{
		
		return ResponseEntity.ok(authServices.createUser(user));
		
	}
	
	@PostMapping("/signIn")
	public ResponseEntity<ResponseEntity<AuthResponse>> signIn(@RequestBody SignInRequest signInRequest,HttpServletResponse response) throws UserNotFound{
		return ResponseEntity.ok(authServices.signIn(signInRequest, response));
		
	}
	

}
