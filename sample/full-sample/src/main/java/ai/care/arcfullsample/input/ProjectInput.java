package ai.care.arcfullsample.input;

import ai.care.arcfullsample.dictionary.ProjectCategory;
import lombok.Data;

import java.util.List;

@Data
public class ProjectInput {
    private String name;
    private String description;
    private ProjectCategory category;
    private List<String> vendorBranches;
}
