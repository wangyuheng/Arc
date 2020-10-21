package a.b.c.type;

import ai.care.arc.dgraph.annotation.DgraphType;
import ai.care.arc.dgraph.annotation.UidField;
import ai.care.arc.dgraph.dictionary.IDgraphType;
import ai.care.arc.graphql.annotation.DataFetcherService;
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