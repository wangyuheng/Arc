package ai.care.arc.graphql.exception;

import ai.care.arc.graphql.interceptor.DataFetcherInterceptorRegistry;
import ai.care.arc.graphql.support.RuntimeWiringRegistry;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.*;
import graphql.language.SourceLocation;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author junhao.chen
 * @date 2020/11/28
 */
@SpringBootTest
public class CustomDataFetcherExceptionHandlerTest {

    private GraphQL graphQL;

    private static final String TEST_SCHEMA = "type Query {\n" +
            "                customException : String\n" +
            "                runtimeException : String\n" +
            "            }";

    @Before
    public void init(){
        Map<String, DataFetcher> decoratorMap = new HashMap<>();
        decoratorMap.put("customException", dataFetchingEnvironment -> {
            throw new TestCustomException("custom error");
        });
        decoratorMap.put("runtimeException", dataFetchingEnvironment -> {
            throw new RuntimeException("runtime error");
        });
        RuntimeWiring.newRuntimeWiring().type(TypeRuntimeWiring.newTypeWiring("Query").dataFetchers(decoratorMap)).build();
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(TEST_SCHEMA);
        GraphQLSchema graphQLSchema = new SchemaGenerator()
                .makeExecutableSchema(typeRegistry, RuntimeWiring.newRuntimeWiring().type(TypeRuntimeWiring.newTypeWiring("Query").dataFetchers(decoratorMap)).build());
        this.graphQL = GraphQL
                .newGraphQL(graphQLSchema)
                .queryExecutionStrategy(new AsyncExecutionStrategy(new CustomDataFetcherExceptionHandler()))
                .mutationExecutionStrategy(new AsyncExecutionStrategy(new CustomDataFetcherExceptionHandler()))
                .build();
    }

    class TestCustomException extends CustomException{

        TestCustomException(String message) {
            super(message);
        }
    }

    @Test
    public void test_custom_exception() {
        ExecutionResult result = graphQL.execute(ExecutionInput.newExecutionInput("{ customException }"));
        assertNotNull(result.getErrors());
        assertEquals(1,result.getErrors().size());
        assertEquals("Exception while fetching data (/customException) : custom error",result.getErrors().get(0).getMessage());
    }

    @Test
    public void test_runtime_exception(){
        ExecutionResult result = graphQL.execute(ExecutionInput.newExecutionInput("{ runtimeException }"));
        assertNotNull(result.getErrors());
        assertEquals(1,result.getErrors().size());
        assertNotEquals("Exception while fetching data (/runtimeException) : runtime error",result.getErrors().get(0).getMessage());
        assertEquals("Exception while fetching data (/runtimeException) : general exception",result.getErrors().get(0).getMessage());
    }
}
