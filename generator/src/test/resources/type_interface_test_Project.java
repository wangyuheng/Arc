package a.b.c.type;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.annotation.UidField;
import com.github.yituhealthcare.arc.dgraph.dictionary.IDomainClass;
import com.github.yituhealthcare.arc.graphql.annotation.Graphql;
import java.lang.Object;
import java.lang.String;
import java.util.List;
import java.util.Map;

/**
 * Project
 * Generate with GraphQL Schema By Arc
 */
@Graphql
@DgraphType("PROJECT")
public class Project implements IDomainClass, Entity {
  /**
   * id
   */
  @UidField
  private String id;

  /**
   * names
   */
  private List<String> names;

  /**
   * json
   */
  private Map<String, Object> json;

  /**
   * jsonArray
   */
  private List<Map<String, Object>> jsonArray;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setNames(List<String> names) {
    this.names = names;
  }

  public List<String> getNames() {
    return names;
  }

  public void setJson(Map<String, Object> json) {
    this.json = json;
  }

  public Map<String, Object> getJson() {
    return json;
  }

  public void setJsonArray(List<Map<String, Object>> jsonArray) {
    this.jsonArray = jsonArray;
  }

  public List<Map<String, Object>> getJsonArray() {
    return jsonArray;
  }
}