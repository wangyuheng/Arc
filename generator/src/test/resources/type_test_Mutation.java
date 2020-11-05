package a.b.c.type;

import a.b.c.api.MutationService;
import ai.care.arc.dgraph.dictionary.IDgraphType;
import ai.care.arc.graphql.annotation.DataFetcherService;
import ai.care.arc.graphql.annotation.GraphqlMethod;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mutation
 * Generate with GraphQL Schema By Arc
 */
@DataFetcherService
public class Mutation implements IDgraphType {
  @Autowired
  private MutationService mutationService;
  
  @GraphqlMethod(
    type = "Mutation"
  )
  public DataFetcher<Project> createProject() {
    return dataFetchingEnvironment ->  {
      return mutationService.handleCreateProject(dataFetchingEnvironment);
    };
  }
  
  @GraphqlMethod(
    type = "Mutation"
  )
  public DataFetcher<Milestone> createMilestone() {
    return dataFetchingEnvironment ->  {
      return mutationService.handleCreateMilestone(dataFetchingEnvironment);
    };
  }
}