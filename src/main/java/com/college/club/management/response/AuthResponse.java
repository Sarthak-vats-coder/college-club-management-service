package com.college.club.management.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {
	private String jwt; 
    private String message; 
    private Boolean status;
    public String getMessage() { 
        return message; 
    } 
    public void setMessage(String message) { 
        this.message = message; 
    } 
    public boolean isStatus() { 
        return status; 
    } 
    public void setStatus(boolean status) { 
        this.status = status; 
    } 
  

}
