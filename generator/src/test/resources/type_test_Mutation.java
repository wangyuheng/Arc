package a.b.c.type;

import a.b.c.api.MutationService;
import com.github.yituhealthcare.arc.dgraph.dictionary.IDgraphType;
import com.github.yituhealthcare.arc.graphql.annotation.Graphql;
import com.github.yituhealthcare.arc.graphql.annotation.GraphqlMethod;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mutation
 * Generate with GraphQL Schema By Arc
 */
@Graphql
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