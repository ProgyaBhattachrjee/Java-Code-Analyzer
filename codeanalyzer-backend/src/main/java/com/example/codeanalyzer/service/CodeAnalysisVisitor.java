package com.example.codeanalyzer.service;


import com.example.codeanalyzer.model.AnalysisResponse;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CodeAnalysisVisitor extends VoidVisitorAdapter<AnalysisResponse> {

    private int currentDepth = 0;
    private int maxDepth = 0;

    @Override
    public void visit(MethodDeclaration n, AnalysisResponse arg) {
        arg.setMethodCount(arg.getMethodCount() + 1);

        int lines = n.getRange().map(r -> r.end.line - r.begin.line).orElse(0);

        if (lines > 30) {
            arg.getCodeSmells().add("Method '" + n.getNameAsString() + "' is too long.");
        }

        if (n.getParameters().size() > 4) {
            arg.getCodeSmells().add("Too many parameters in method '" + n.getNameAsString() + "'");
        }

        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, AnalysisResponse arg) {
        handleLoop(arg);
        super.visit(n, arg);
        exitLoop();
    }

    @Override
    public void visit(WhileStmt n, AnalysisResponse arg) {
        handleLoop(arg);
        super.visit(n, arg);
        exitLoop();
    }

    @Override
    public void visit(DoStmt n, AnalysisResponse arg) {
        handleLoop(arg);
        super.visit(n, arg);
        exitLoop();
    }

    private void handleLoop(AnalysisResponse arg) {
        arg.setLoopCount(arg.getLoopCount() + 1);
        currentDepth++;
        maxDepth = Math.max(maxDepth, currentDepth);
    }

    private void exitLoop() {
        currentDepth--;
    }

    @Override
    public void visit(IfStmt n, AnalysisResponse arg) {
        arg.setIfCount(arg.getIfCount() + 1);

        if (currentDepth >= 3) {
            arg.getCodeSmells().add("Deep nesting detected");
        }

        super.visit(n, arg);
    }
    @Override
    public void visit(MethodCallExpr n, AnalysisResponse arg) {
        String name = n.getNameAsString();

        if (name.equals("contains") && currentDepth > 0) {
            arg.getCodeSmells().add("Possible performance issue: contains() inside loop");
        }

        super.visit(n, arg);
    }
    public void calculateComplexity(AnalysisResponse arg) {
        if (maxDepth == 0) arg.setTimeComplexity("O(1)");
        else if (maxDepth == 1) arg.setTimeComplexity("O(n)");
        else arg.setTimeComplexity("O(n^" + maxDepth + ")");
        if (arg.getLoopCount() > 0) arg.setSpaceComplexity("O(n)");
        else arg.setSpaceComplexity("O(1)");
    }
}