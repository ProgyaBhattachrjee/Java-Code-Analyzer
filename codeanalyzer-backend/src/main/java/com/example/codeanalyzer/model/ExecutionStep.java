package com.example.codeanalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExecutionStep {
    private int step;
    private String description;
}