package ai.care.generatorsample.generated.type;

import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.dictionary.IDgraphType;
import ai.care.arc.graphql.annotation.DataFetcherService;
import java.lang.String;

/**
 * User
 * Generate with GraphQL Schema By Arc
 */
@DataFetcherService
@DgraphType("USER")
public class User implements IDgraphType {
  /**
   * name
   */
  private String name;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
