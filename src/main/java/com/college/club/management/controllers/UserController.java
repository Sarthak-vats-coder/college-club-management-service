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
import com.college.club.management.services.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RestControllerAdvice
@RequestMapping("/user")
public class UserController {
	UserServices userServices;

	public UserController(UserServices userServices) {
		this.userServices = userServices;
	}

	@PostMapping(value = "/createClub", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Club> createClub(
	        @RequestParam("club") String clubJson,
	        @RequestParam("profilePicture") MultipartFile profilePicture,
	        HttpServletRequest request) throws Exception {
	    
	    Club club = new ObjectMapper().readValue(clubJson, Club.class);

	    return ResponseEntity.ok(userServices.createClub(club, profilePicture, request));
	}
	
	@PostMapping("/getUser")
	public ResponseEntity<User> getUser(@RequestParam String username) throws UserNotFound {
		return ResponseEntity.ok(userServices.fetchUserDetails(username)) ;
	}
	
}
