package com.college.club.management.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.college.club.management.entities.Club;
import com.college.club.management.entities.User;
import com.college.club.management.exception.UserNotFound;

import jakarta.servlet.http.HttpServletRequest;
@Service
public interface UserServices {
	Club createClub(Club club,MultipartFile profilePicture,HttpServletRequest request) throws UserNotFound;
	User fetchUserDetails(String username) throws UserNotFound;
}
