package com.college.club.management.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.college.club.management.entities.Club;
import com.college.club.management.services.RootAdminServices;


@RestController
@RestControllerAdvice
@RequestMapping("/view")
public class ViewDataController {
	
	RootAdminServices rootAdminServices;
	public  ViewDataController(RootAdminServices rootAdminServices) {
			this.rootAdminServices =rootAdminServices;
	}
	
	@GetMapping("/allClubs")
	public ResponseEntity<List<Club>> getMethodName() {
		return ResponseEntity.ok(rootAdminServices.getAllClub());
	}
	

}
