package a.b.c.type;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.dictionary.IDomainClass;
import com.github.yituhealthcare.arc.graphql.annotation.Graphql;
import java.lang.String;

/**
 * User
 * Generate with GraphQL Schema By Arc
 */
@Graphql
@DgraphType("USER")
public class User implements IDomainClass {
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