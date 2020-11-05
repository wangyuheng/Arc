package a.b.c.api;
import a.b.c.type.Project;
import graphql.schema.DataFetchingEnvironment;
/**
 * Generate with GraphQL Schema By Arc
 */
public interface QueryService {
  Project handleProject(DataFetchingEnvironment dataFetchingEnvironment);
}