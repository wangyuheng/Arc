package ai.care.arc.graphql.event;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomainEvent {
    private ExecutionInput executionInput;
    private ExecutionResult executionResult;
}