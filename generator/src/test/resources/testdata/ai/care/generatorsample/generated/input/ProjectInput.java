package ai.care.generatorsample.generated.input;

import ai.care.generatorsample.generated.dictionary.ProjectCategory;
import java.lang.String;
import java.util.List;

/**
 * ProjectInput
 * Generate with GraphQL Schema By Arc
 */
public class ProjectInput {
  /**
   * name
   */
  private String name;

  /**
   * description
   */
  private String description;

  /**
   * vendorBranches
   */
  private List<String> vendorBranches;

  /**
   * category
   */
  private ProjectCategory category;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setVendorBranches(List<String> vendorBranches) {
    this.vendorBranches = vendorBranches;
  }

  public List<String> getVendorBranches() {
    return vendorBranches;
  }

  public void setCategory(ProjectCategory category) {
    this.category = category;
  }

  public ProjectCategory getCategory() {
    return category;
  }
}
