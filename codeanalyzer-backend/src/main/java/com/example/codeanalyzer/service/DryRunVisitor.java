package com.example.codeanalyzer.service;

import java.util.*;

import com.example.codeanalyzer.model.ExecutionStep;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class DryRunVisitor extends VoidVisitorAdapter<Void> {

    private List<ExecutionStep> steps = new ArrayList<>();
    private Map<String, Integer> variables = new HashMap<>();
    private int stepCount = 1;

    private void addStep(String desc) {
        steps.add(new ExecutionStep(stepCount++, desc));
    }

    public List<ExecutionStep> getSteps() {
        return steps;
    }

    @Override
    public void visit(ExpressionStmt n, Void arg) {

    	if (n.getExpression() instanceof AssignExpr assign) {
    	    String var = assign.getTarget().toString();
    	    int value = evaluate(assign.getValue());

    	    variables.put(var, value);
    	    addStep(var + " = " + value);
    	}
        super.visit(n, arg);
    }

    @Override
    public void visit(IfStmt n, Void arg) {

        boolean condition = evaluateCondition(n.getCondition());

        addStep("Check (" + n.getCondition() + ") → " + condition);

        if (condition) {
            n.getThenStmt().accept(this, arg);
        } else if (n.getElseStmt().isPresent()) {
            n.getElseStmt().get().accept(this, arg);
        }
    }

    @Override
    public void visit(ForStmt n, Void arg) {

        // init (int i = 0)
        n.getInitialization().forEach(init -> init.accept(this, arg));

        for (int i = 0; i < 3; i++) { // simulate 3 iterations

            boolean condition = n.getCompare()
                    .map(this::evaluateCondition)
                    .orElse(true);

            if (!condition) break;

            addStep("Iteration " + (i + 1));

            n.getBody().accept(this, arg);

            // update (i++)
            n.getUpdate().forEach(update -> update.accept(this, arg));
        }
    }
    @Override
    public void visit(VariableDeclarationExpr n, Void arg) {
        n.getVariables().forEach(var -> {
            String name = var.getNameAsString();
            int value = var.getInitializer()
                    .map(this::evaluate)
                    .orElse(0);

            variables.put(name, value);
            addStep(name + " = " + value);
        });
    }
    private int evaluate(Expression expr) {

        if (expr.isIntegerLiteralExpr()) {
            return Integer.parseInt(expr.toString());
        }

        if (expr.isNameExpr()) {
            return variables.getOrDefault(expr.toString(), 0);
        }

        if (expr.isBinaryExpr()) {
            BinaryExpr bin = expr.asBinaryExpr();

            int left = evaluate(bin.getLeft());
            int right = evaluate(bin.getRight());

            return switch (bin.getOperator()) {
                case PLUS -> left + right;
                case MINUS -> left - right;
                case MULTIPLY -> left * right;
                case DIVIDE -> right != 0 ? left / right : 0;
                default -> 0;
            };
        }

        return 0;
    }
    @Override
    public void visit(UnaryExpr n, Void arg) {

        String var = n.getExpression().toString();
        int value = variables.getOrDefault(var, 0);

        if (n.getOperator() == UnaryExpr.Operator.POSTFIX_INCREMENT ||
            n.getOperator() == UnaryExpr.Operator.PREFIX_INCREMENT) {
            value++;
        } else if (n.getOperator() == UnaryExpr.Operator.POSTFIX_DECREMENT ||
                   n.getOperator() == UnaryExpr.Operator.PREFIX_DECREMENT) {
            value--;
        }

        variables.put(var, value);
        addStep(var + " = " + value);
    }
    private boolean evaluateCondition(Expression expr) {
        // VERY BASIC: only handles x > number
        if (expr instanceof BinaryExpr bin) {
            int left = evaluate(bin.getLeft());
            int right = evaluate(bin.getRight());

            return switch (bin.getOperator()) {
                case GREATER -> left > right;
                case LESS -> left < right;
                case EQUALS -> left == right;
                default -> false;
            };
        }
        return false;
    }
}