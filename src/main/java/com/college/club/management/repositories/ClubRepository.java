package com.college.club.management.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.college.club.management.entities.Club;

public interface ClubRepository extends MongoRepository<Club, String>{
	Club findByOwnerId(String ownerId);

}
