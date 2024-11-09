package com.college.club.management.serviceImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.college.club.management.config.JwtProvider;
import com.college.club.management.config.JwtValidator;
import com.college.club.management.entities.Club;
import com.college.club.management.exception.ClubNotFound;
import com.college.club.management.exception.UserNotFound;
import com.college.club.management.repositories.ClubRepository;
import com.college.club.management.services.ClubServices;
import com.college.club.management.services.RootAdminServices;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ClubServiceImpl implements ClubServices {
	ClubRepository clubRepository;
	JwtValidator jwtValidator;
	JwtProvider jwtProvider;
	RootAdminServices rootAdminServices;
	

	public ClubServiceImpl(RootAdminServices rootAdminServices,ClubRepository clubRepository, JwtValidator jwtValidator, JwtProvider jwtProvider) {
		this.clubRepository = clubRepository;
		this.jwtProvider = jwtProvider;
		this.jwtValidator = jwtValidator;
		this.rootAdminServices = rootAdminServices;
	}

	@Override
	public Club updateClub(Club club, HttpServletRequest request) throws UserNotFound, ClubNotFound {
		
		Cookie authcookie = jwtValidator.getAuthCookie(request);
		String username = jwtProvider.extractUsername(authcookie.getValue());
		String ownerId = rootAdminServices.findUserByUsername(username).getId();
		Club updateClub = rootAdminServices.findClubByOwnerId(ownerId);
		updateClub.setName(club.getName());
		updateClub.setDescription(club.getDescription());
		updateClub.setOwnerId(ownerId);
		return clubRepository.save(updateClub);
	}

}
