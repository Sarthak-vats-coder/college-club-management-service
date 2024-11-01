package com.college.club.management.services;


import org.springframework.http.ResponseEntity;


import com.college.club.management.entities.User;
import com.college.club.management.exception.UserNotFound;
import com.college.club.management.models.SignInRequest;
import com.college.club.management.response.AuthResponse;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthServices {
	 User createUser(User user) throws Exception;
	 ResponseEntity<AuthResponse> signIn(SignInRequest userRequest, HttpServletResponse response) throws UserNotFound;

}
