package com.example.codeanalyzer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

	@GetMapping("/health")
	public String Test() {
		return "Backend is running";
	}
}
