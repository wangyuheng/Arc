package com.github.yituhealthcare.arc.graphql.event;

import graphql.ExecutionInput;
import graphql.ExecutionResult;

public class DomainEvent {
    private ExecutionInput executionInput;
    private ExecutionResult executionResult;

    public DomainEvent(ExecutionInput executionInput, ExecutionResult executionResult) {
        this.executionInput = executionInput;
        this.executionResult = executionResult;
    }

    public ExecutionInput getExecutionInput() {
        return this.executionInput;
    }

    public ExecutionResult getExecutionResult() {
        return this.executionResult;
    }

    public void setExecutionInput(ExecutionInput executionInput) {
        this.executionInput = executionInput;
    }

    public void setExecutionResult(ExecutionResult executionResult) {
        this.executionResult = executionResult;
    }

    public String toString() {
        return "DomainEvent(executionInput=" + this.getExecutionInput() + ", executionResult=" + this.getExecutionResult() + ")";
    }
}