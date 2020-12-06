package com.github.yituhealthcare.arcfullsample.domain;

import com.github.yituhealthcare.arc.dgraph.annotation.DgraphType;
import com.github.yituhealthcare.arc.dgraph.annotation.UidField;
import com.github.yituhealthcare.arc.graphql.annotation.Graphql;
import com.github.yituhealthcare.arcfullsample.dictionary.MilestoneStatus;

@Graphql
@DgraphType("MILESTONE")
public class Milestone  {

    public Milestone(String name) {
        this.name = name;
    }

    @UidField
    private String id;
    private String name;

    private MilestoneStatus status;

    public Milestone() {
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public MilestoneStatus getStatus() {
        return this.status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(MilestoneStatus status) {
        this.status = status;
    }

    public String toString() {
        return "Milestone(id=" + this.getId() + ", name=" + this.getName() + ", status=" + this.getStatus() + ")";
    }
}
