package com.college.club.management.services;

import java.util.List;

import com.college.club.management.entities.Club;
import com.college.club.management.entities.User;
import com.college.club.management.exception.UserNotFound;


public interface RootAdminServices {
	User findUserByUsername(String username) throws UserNotFound;
	List<User> getAllUser();
	
	List<Club> getAllClub();
}
