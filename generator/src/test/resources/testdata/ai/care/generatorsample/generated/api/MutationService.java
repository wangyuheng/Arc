package ai.care.generatorsample.generated.api;

import ai.care.generatorsample.generated.type.Milestone;
import ai.care.generatorsample.generated.type.Project;
import graphql.schema.DataFetchingEnvironment;

/**
 * Generate with GraphQL Schema By Arc
 */
public interface MutationService {
  Project handleCreateProject(DataFetchingEnvironment dataFetchingEnvironment);

  Milestone handleCreateMilestone(DataFetchingEnvironment dataFetchingEnvironment);
}
