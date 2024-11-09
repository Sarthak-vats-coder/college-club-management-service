package com.college.club.management.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
public class LogController {

	@GetMapping("/all")
	public String allLogs() {
		StringBuilder builder  = new StringBuilder();
		try(BufferedReader reader= new BufferedReader(new FileReader(new File("logs/spring.log")))){
			String line = "";
			while((line=reader.readLine())!=null) {
				builder.append(line).append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.toString();
	}
}
