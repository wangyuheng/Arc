package a.b.c.type;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.dictionary.IDgraphType;
import com.github.yituhealthcare.arc.graphql.annotation.DataFetcherService;
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