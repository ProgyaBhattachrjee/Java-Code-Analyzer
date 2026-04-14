package com.example.codeanalyzer.service;

import java.util.ArrayList;
import java.util.List;

import com.example.codeanalyzer.model.FlowEdge;
import com.example.codeanalyzer.model.FlowNode;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class FlowchartVisitor extends VoidVisitorAdapter<Void> {

    private List<FlowNode> nodes = new ArrayList<>();
    private List<FlowEdge> edges = new ArrayList<>();

    private int nodeId = 1;
    private int prevNode = -1;

    public void start() {
        prevNode = createNode("start", "Start");
    }

    public void end() {
        int end = createNode("end", "End");
        edges.add(new FlowEdge(prevNode, end, ""));
    }

    private int createNode(String type, String label) {
        FlowNode node = new FlowNode(nodeId, type, label);
        nodes.add(node);

        if (prevNode != -1) {
            edges.add(new FlowEdge(prevNode, nodeId, ""));
        }

        prevNode = nodeId;
        return nodeId++;
    }

    @Override
    public void visit(ExpressionStmt n, Void arg) {
        createNode("process", n.toString());
        // ❌ NO super.visit
    }

    @Override
    public void visit(IfStmt n, Void arg) {

        int decision = createNode("decision", n.getCondition().toString());

        // THEN
        prevNode = decision;
        n.getThenStmt().accept(this, arg);
        int thenEnd = prevNode;

        // ELSE
        int elseEnd = decision;
        if (n.getElseStmt().isPresent()) {
            prevNode = decision;
            n.getElseStmt().get().accept(this, arg);
            elseEnd = prevNode;
        }

        // MERGE
        int merge = createNode("process", "merge");

        edges.add(new FlowEdge(thenEnd, merge, "true"));
        edges.add(new FlowEdge(elseEnd, merge, "false"));

        prevNode = merge;
    }

    @Override
    public void visit(ForStmt n, Void arg) {

        int loop = createNode("decision", "for (i < limit)");

        prevNode = loop;
        n.getBody().accept(this, arg);

        // loop back
        edges.add(new FlowEdge(prevNode, loop, "loop"));

        prevNode = loop;
    }

    public List<FlowNode> getNodes() { return nodes; }
    public List<FlowEdge> getEdges() { return edges; }
}