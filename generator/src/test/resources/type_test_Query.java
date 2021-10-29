package a.b.c.type;

import a.b.c.datafetcher.QueryDataFetcher;
import io.github.wangyuheng.arc.dgraph.dictionary.IDomainClass;
import io.github.wangyuheng.arc.graphql.annotation.Graphql;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlMethod;
import graphql.schema.DataFetcher;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Query
 * Generate with GraphQL Schema
 *
 * @author Arc
 */
@Graphql
public class Query implements IDomainClass {
  @Autowired
  private QueryDataFetcher queryDataFetcher;

  @GraphqlMethod(
      type = "Query"
  )
  public DataFetcher<Project> project() {
    return dataFetchingEnvironment -> queryDataFetcher.handleProject(dataFetchingEnvironment);
  }

  @GraphqlMethod(
      type = "Query"
  )
  public DataFetcher<List<User>> users() {
    return dataFetchingEnvironment -> queryDataFetcher.handleUsers(dataFetchingEnvironment);
  }

}