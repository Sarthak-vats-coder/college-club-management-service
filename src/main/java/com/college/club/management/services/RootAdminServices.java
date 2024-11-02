package com.college.club.management.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.college.club.management.entities.Club;
import com.college.club.management.entities.User;
import com.college.club.management.exception.ClubNotFound;
import com.college.club.management.exception.UserNotFound;

@Service
public interface RootAdminServices {
	User findUserByUsername(String username) throws UserNotFound;
	Club findClubByOwnerId(String ownerId) throws ClubNotFound ;
	List<User> getAllUser();
	
	List<Club> getAllClub();
}
