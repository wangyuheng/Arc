package ai.care.generatorsample.generated.api;

import ai.care.generatorsample.generated.type.Project;
import ai.care.generatorsample.generated.type.User;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;

/**
 * Generate with GraphQL Schema By Arc
 */
public interface QueryService {
  Project handleProject(DataFetchingEnvironment dataFetchingEnvironment);

  List<User> handleUsers(DataFetchingEnvironment dataFetchingEnvironment);
}
