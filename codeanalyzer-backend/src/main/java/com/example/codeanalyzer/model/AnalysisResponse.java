package com.example.codeanalyzer.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalysisResponse {
    private long lineCount;
    private long wordCount;
    private long charCount;
    private long loopCount;
    private long methodCount;
    private long ifCount;
    private String timeComplexity;
    private String spaceComplexity;
    private List<String> codeSmells;
    private List<FlowNode> nodes;
    private List<FlowEdge> edges;
    private List<ExecutionStep> executionSteps;
}
