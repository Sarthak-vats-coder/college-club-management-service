package com.college.club.management.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.college.club.management.entities.User;

public interface UserRepository extends MongoRepository<User,String> {
	Optional<User> findByUsername(String Username);

}
