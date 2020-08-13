package ai.care.arcgraphqlsample.input;

import lombok.Data;

import java.util.List;

@Data
public class ProjectInput {
    private String name;
    private String description;
    private String dsl;
    private List<String> vendorBranches;
}
