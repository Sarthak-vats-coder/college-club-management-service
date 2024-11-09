package com.college.club.management.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.college.club.management.entities.Club;
import com.college.club.management.exception.ClubNotFound;
import com.college.club.management.exception.UserNotFound;
import com.college.club.management.services.ClubServices;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RestControllerAdvice
@RequestMapping("/club")
public class ClubController {
	ClubServices clubServices;
	public ClubController(ClubServices clubServices) {
		this.clubServices = clubServices;
	}
	@PostMapping("/updateClub")
	public ResponseEntity<Club> updateClub(@RequestBody Club club, HttpServletRequest request) throws UserNotFound, ClubNotFound {
		return ResponseEntity.ok(clubServices.updateClub(club, request));
	}
	

}
