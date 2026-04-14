package com.example.codeanalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlowNode {
    private int id;
    private String type;
    private String label;
}