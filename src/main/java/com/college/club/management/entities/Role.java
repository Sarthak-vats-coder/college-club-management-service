package com.college.club.management.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Document(collection="roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	@Id
    private String id;
    private String name;
    
    public Role(String name) {
        this.name = name;
    }
}
