package a.b.c.datafetcher;
import a.b.c.type.Project;
import graphql.schema.DataFetchingEnvironment;
/**
 * Generate with GraphQL Schema By Arc
 */
public interface MutationDataFetcher {
  Project handleCreateProject(DataFetchingEnvironment dataFetchingEnvironment);
}