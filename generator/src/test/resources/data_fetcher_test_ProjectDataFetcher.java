package a.b.c.datafetcher;
import a.b.c.type.Milestone;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
/**
 * Generate with GraphQL Schema By Arc
 */
public interface ProjectDataFetcher {
  List<Milestone> handleMilestones(DataFetchingEnvironment dataFetchingEnvironment);
}