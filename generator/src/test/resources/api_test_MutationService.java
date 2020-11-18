package a.b.c.api;
import a.b.c.type.Project;
import graphql.schema.DataFetchingEnvironment;
/**
 * Generate with GraphQL Schema By Arc
 */
public interface MutationService {
  Project handleCreateProject(DataFetchingEnvironment dataFetchingEnvironment);
}