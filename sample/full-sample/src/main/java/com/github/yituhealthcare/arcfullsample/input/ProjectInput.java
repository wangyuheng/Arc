package io.github.wangyuheng.arcfullsample.input;

import io.github.wangyuheng.arcfullsample.dictionary.ProjectCategory;

import java.util.List;

public class ProjectInput {
    private String name;
    private String description;
    private ProjectCategory category;
    private List<String> vendorBranches;

    public ProjectInput() {
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public ProjectCategory getCategory() {
        return this.category;
    }

    public List<String> getVendorBranches() {
        return this.vendorBranches;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(ProjectCategory category) {
        this.category = category;
    }

    public void setVendorBranches(List<String> vendorBranches) {
        this.vendorBranches = vendorBranches;
    }

    public String toString() {
        return "ProjectInput(name=" + this.getName() + ", description=" + this.getDescription() + ", category=" + this.getCategory() + ", vendorBranches=" + this.getVendorBranches() + ")";
    }
}
