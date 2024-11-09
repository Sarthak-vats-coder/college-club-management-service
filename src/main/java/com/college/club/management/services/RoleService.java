package com.college.club.management.services;

import org.springframework.stereotype.Service;

import com.college.club.management.entities.Role;
@Service
public interface RoleService {
	Role createNewRole (String role);
}
