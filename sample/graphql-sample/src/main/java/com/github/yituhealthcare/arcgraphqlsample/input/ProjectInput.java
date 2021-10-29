package io.github.wangyuheng.arcgraphqlsample.input;

import java.util.List;

public class ProjectInput {
    private String name;
    private String description;
    private String dsl;
    private List<String> vendorBranches;

    public ProjectInput() {
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDsl() {
        return this.dsl;
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

    public void setDsl(String dsl) {
        this.dsl = dsl;
    }

    public void setVendorBranches(List<String> vendorBranches) {
        this.vendorBranches = vendorBranches;
    }

    public String toString() {
        return "ProjectInput(name=" + this.getName() + ", description=" + this.getDescription() + ", dsl=" + this.getDsl() + ", vendorBranches=" + this.getVendorBranches() + ")";
    }
}
