package com.college.club.management.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.NonNull;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1050193029199633279L;
	@Id
	private String id;
	@Pattern(regexp = "[\\w]{3,20}")
	@Indexed(unique = true)
	@NonNull
	private String username;
	@NonNull
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	@DBRef
	private Set <Role> roles;
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return new ArrayList<GrantedAuthority>();
	}
}
