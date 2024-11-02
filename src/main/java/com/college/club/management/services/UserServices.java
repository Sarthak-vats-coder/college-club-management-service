package com.college.club.management.services;

import org.springframework.stereotype.Service;

import com.college.club.management.entities.Club;
import com.college.club.management.exception.UserNotFound;

import jakarta.servlet.http.HttpServletRequest;
@Service
public interface UserServices {
	Club createClub(Club club,HttpServletRequest request) throws UserNotFound;
}
