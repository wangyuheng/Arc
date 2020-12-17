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
 * Generate with GraphQL Schema
 *
 * @author Arc
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

  public Project(String id, List<String> names, Map<String, Object> json,
                 List<Map<String, Object>> jsonArray) {
    this.id = id;
    this.names = names;
    this.json = json;
    this.jsonArray = jsonArray;
  }

  public Project() {
  }

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

  public static ProjectBuilder builder() {
    return new ProjectBuilder();
  }

  public static class ProjectBuilder {
    private String id;

    private List<String> names;

    private Map<String, Object> json;

    private List<Map<String, Object>> jsonArray;

    private ProjectBuilder() {
    }

    public ProjectBuilder id(String id) {
      this.id = id;
      return this;
    }

    public ProjectBuilder names(List<String> names) {
      this.names = names;
      return this;
    }

    public ProjectBuilder json(Map<String, Object> json) {
      this.json = json;
      return this;
    }

    public ProjectBuilder jsonArray(List<Map<String, Object>> jsonArray) {
      this.jsonArray = jsonArray;
      return this;
    }

    public Project build() {
      return new Project(id,names,json,jsonArray);
    }
  }
}