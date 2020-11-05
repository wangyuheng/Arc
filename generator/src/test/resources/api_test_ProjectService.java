package a.b.c.api;
import a.b.c.type.Milestone;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
/**
 * Generate with GraphQL Schema By Arc
 */
public interface ProjectService {
  List<Milestone> handleMilestones(DataFetchingEnvironment dataFetchingEnvironment);
}