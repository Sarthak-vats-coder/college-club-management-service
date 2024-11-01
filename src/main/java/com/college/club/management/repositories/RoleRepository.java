package com.college.club.management.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.college.club.management.entities.Role;

public interface RoleRepository extends MongoRepository<Role, String>{
	Optional<Role> findByName(String name);
}
