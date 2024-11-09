package com.college.club.management.serviceImpl;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.college.club.management.config.JwtProvider;
import com.college.club.management.config.JwtValidator;
import com.college.club.management.entities.Club;
import com.college.club.management.entities.Role;
import com.college.club.management.entities.User;
import com.college.club.management.exception.UserNotFound;
import com.college.club.management.repositories.ClubRepository;
import com.college.club.management.repositories.RoleRepository;
import com.college.club.management.repositories.UserRepository;
import com.college.club.management.services.RoleService;
import com.college.club.management.services.RootAdminServices;
import com.college.club.management.services.UserServices;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
@Service
public class UserServiceImpl implements UserServices{
	UserRepository userRepository;
	RoleRepository roleRepository;
	ClubRepository clubRepository;
	PasswordEncoder passwordEncoder;
	RootAdminServices rootAdminServices;
	JwtProvider jwtProvider;
	JwtValidator jwtValidator;
	RoleService roleService;

	public UserServiceImpl(RoleService roleService,ClubRepository clubRepository,JwtValidator jwtValidator,JwtProvider jwtProvider,PasswordEncoder passwordEncoder, UserRepository userRepository,RoleRepository roleRepository,RootAdminServices rootAdminServices) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.clubRepository = clubRepository;
		this.jwtProvider = jwtProvider;
		this.rootAdminServices = rootAdminServices;
		this.jwtValidator=jwtValidator;
		this.roleService =roleService;
	}
	@Override
	public Club createClub(Club club,MultipartFile profilePicture,HttpServletRequest request) throws UserNotFound {
			Role clubAdminRole = roleService.createNewRole("CLUB_ADMIN");
		    Cookie authCookie = jwtValidator.getAuthCookie(request);
		    String username = jwtProvider.extractUsername(authCookie.getValue());
		    User user = rootAdminServices.findUserByUsername(username);
		    String ownerid = user.getId();
		     if (!user.getRoles().contains(clubAdminRole)) {
		    	 user.getRoles().add(clubAdminRole);
		    	 userRepository.save(user);
		     }
		    
		    Club newClub = new Club();
		    newClub.setName(club.getName());
		    newClub.setDescription(club.getDescription());
		    newClub.setOwnerId(ownerid);
		    
		    try {
		        if (profilePicture != null && !profilePicture.isEmpty()) {
		            newClub.setProfilePicture(profilePicture.getBytes());
		        }
		    } catch (IOException e) {
		        throw new RuntimeException("Failed to save profile picture", e);
		    }
		    return clubRepository.save(newClub);
	}
	@Override
	public User fetchUserDetails(String username) throws UserNotFound {
		
		return rootAdminServices.findUserByUsername(username);
	}

}
