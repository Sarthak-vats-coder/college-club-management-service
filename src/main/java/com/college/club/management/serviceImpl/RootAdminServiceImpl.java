package com.college.club.management.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.college.club.management.entities.Club;
import com.college.club.management.entities.User;
import com.college.club.management.exception.ClubNotFound;
import com.college.club.management.exception.UserNotFound;
import com.college.club.management.repositories.ClubRepository;
import com.college.club.management.repositories.RoleRepository;
import com.college.club.management.repositories.UserRepository;
import com.college.club.management.services.RootAdminServices;
@Service
public class RootAdminServiceImpl implements RootAdminServices{
	
	UserRepository userRepository;
	RoleRepository roleRepository;
	ClubRepository clubRepository;

	public RootAdminServiceImpl(ClubRepository clubRepository, UserRepository userRepository,RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.clubRepository =clubRepository;
	}
	@Override
	public User findUserByUsername(String username) throws UserNotFound {
		User user = null;
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isEmpty()) {
			throw new UserNotFound("User not found");
		} else {
			user = optionalUser.get();
		}
		return user;

	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Club> getAllClub() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Club findClubByOwnerId(String ownerId) throws ClubNotFound {
		Club club = null;
		Optional<Club> optionalClub = clubRepository.findByOwnerId(ownerId);
		if(optionalClub.isEmpty()) {
			throw new ClubNotFound("Club not Found");
			
		}else {
			club = optionalClub.get();
		}
		return club;
	}

}
