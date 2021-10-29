package a.b.c.type;

import a.b.c.datafetcher.MutationDataFetcher;
import io.github.wangyuheng.arc.dgraph.dictionary.IDomainClass;
import io.github.wangyuheng.arc.graphql.annotation.Graphql;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlMethod;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mutation
 * Generate with GraphQL Schema
 *
 * @author Arc
 */
@Graphql
public class Mutation implements IDomainClass {
  @Autowired
  private MutationDataFetcher mutationDataFetcher;
  
  @GraphqlMethod(
    type = "Mutation"
  )
  public DataFetcher<Project> createProject() {
    return dataFetchingEnvironment -> mutationDataFetcher.handleCreateProject(dataFetchingEnvironment);
  }
  
  @GraphqlMethod(
    type = "Mutation"
  )
  public DataFetcher<Milestone> createMilestone() {
    return dataFetchingEnvironment -> mutationDataFetcher.handleCreateMilestone(dataFetchingEnvironment);
  }
}