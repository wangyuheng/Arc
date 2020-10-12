package ai.care.generatorsample.generated.api;

import ai.care.generatorsample.generated.type.Milestone;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

/**
 * Generate with GraphQL Schema By Arc
 */
public interface ProjectService {
  List<Milestone> handleMilestones(DataFetchingEnvironment dataFetchingEnvironment);
}
