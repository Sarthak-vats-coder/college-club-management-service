package com.college.club.management.serviceImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.college.club.management.entities.Role;
import com.college.club.management.repositories.RoleRepository;
import com.college.club.management.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	RoleRepository roleRepository;
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Role createNewRole(String role) {
		Optional<Role> userRoleOptional = roleRepository.findByName(role);
	    
	    Role newRole;
	    if (userRoleOptional.isEmpty()) {
	    	newRole = new Role(role);
	    	newRole = roleRepository.save(newRole);  
	        System.out.println(role +"role created and saved to database.");
	    } else {
	    	newRole = userRoleOptional.get();
	        System.out.println(role+" role found in the database.");
	    }		return newRole;
	}

}
