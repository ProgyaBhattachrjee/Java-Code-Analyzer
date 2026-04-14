package com.example.codeanalyzer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codeanalyzer.model.AnalysisResponse;
import com.example.codeanalyzer.model.CodeRequest;
import com.example.codeanalyzer.service.CodeAnalyzerService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CodeAnalyzerController {
	@Autowired
	private CodeAnalyzerService codeAnalyzerService;
	
	@PostMapping
	public AnalysisResponse analysisResponse(@RequestBody CodeRequest code) {
		return codeAnalyzerService.analyze(code.getCode());
	}

}
