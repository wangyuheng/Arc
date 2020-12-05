package a.b.c.type;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.annotation.UidField;
import com.github.yituhealthcare.arc.dgraph.dictionary.IDgraphType;
import com.github.yituhealthcare.arc.graphql.annotation.DataFetcherService;
import java.lang.String;

/**
 * Milestone
 * Generate with GraphQL Schema By Arc
 */
@DataFetcherService
@DgraphType("MILESTONE")
public class Milestone implements IDgraphType {
  /**
   * id
   */
  @UidField
  private String id;

  /**
   * name
   */
  private String name;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}