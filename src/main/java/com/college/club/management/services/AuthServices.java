package com.college.club.management.services;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.college.club.management.entities.User;
import com.college.club.management.exception.UserNotFound;
import com.college.club.management.models.SignInRequest;
import com.college.club.management.response.AuthResponse;

import jakarta.servlet.http.HttpServletResponse;
@Service
public interface AuthServices {
	 User createUser(User user,MultipartFile profilePicture) throws Exception;
	 ResponseEntity<User> signIn(SignInRequest userRequest, HttpServletResponse response) throws UserNotFound;
	 ResponseEntity<AuthResponse> logOut(HttpServletResponse response);
}
