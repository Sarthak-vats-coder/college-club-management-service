package com.college.club.management.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.college.club.management.entities.Club;

public interface ClubRepository extends MongoRepository<Club, String>{
	Optional<Club> findByOwnerId(String ownerId);

}
