package com.example.codeanalyzer.service;

import org.springframework.stereotype.Service;

import com.example.codeanalyzer.model.AnalysisResponse;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

@Service
public class CodeAnalyzerService {

    public AnalysisResponse analyze(String code) {

    	AnalysisResponse response = new AnalysisResponse(
    		    0, 0, 0,
    		    0, 0, 0,
    		    "", "",
    		    new java.util.ArrayList<>(),
    		    null,
    		    new java.util.ArrayList<>(),
    		    new java.util.ArrayList<>()
    		);

        try {
            CompilationUnit cu = StaticJavaParser.parse(code);
            FlowchartVisitor visitor1 = new FlowchartVisitor();

            visitor1.start();
            cu.accept(visitor1, null);
            visitor1.end();

            response.setNodes(visitor1.getNodes());
            response.setEdges(visitor1.getEdges());
            DryRunVisitor dryRun = new DryRunVisitor();
            cu.accept(dryRun, null);

            response.setExecutionSteps(dryRun.getSteps());
            CodeAnalysisVisitor visitor = new CodeAnalysisVisitor();
            cu.accept(visitor, response);
             
            visitor.calculateComplexity(response);

            response.setLineCount(code.lines().count());
            response.setCharCount(code.length());
            response.setWordCount(code.split("\\s+").length);

        } catch (Exception e) {
            response.getCodeSmells().add("Invalid Java code");
        }

        return response;
    }
}