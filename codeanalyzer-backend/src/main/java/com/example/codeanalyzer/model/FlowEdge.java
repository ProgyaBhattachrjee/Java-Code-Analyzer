package com.example.codeanalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlowEdge {
    private int from;
    private int to;
    private String condition;
}