package com.github.yituhealthcare.arc.dgraph.repository;

import com.github.yituhealthcare.arc.dgraph.util.RDFUtil;

import java.util.List;
import java.util.stream.Collectors;

public class RelationshipInformation {

    private List<String> sourceList;
    private String relationship;
    private List<String> targetList;

    public RelationshipInformation(List<String> sourceList, String relationship, List<String> targetList) {
        this.sourceList = sourceList;
        this.relationship = relationship;
        this.targetList = targetList;
    }

    public RelationshipInformation() {
    }

    public static RelationshipInformationBuilder builder() {
        return new RelationshipInformationBuilder();
    }

    public String toRdf() {
        return this.getSourceList().stream()
                .flatMap(s -> this.getTargetList().stream()
                        .map(t -> RDFUtil.wrapperAndJoin(s, this.getRelationship(), t))
                )
                .collect(Collectors.joining("\n"));
    }

    public List<String> getSourceList() {
        return this.sourceList;
    }

    public String getRelationship() {
        return this.relationship;
    }

    public List<String> getTargetList() {
        return this.targetList;
    }

    public void setSourceList(List<String> sourceList) {
        this.sourceList = sourceList;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setTargetList(List<String> targetList) {
        this.targetList = targetList;
    }

    public String toString() {
        return "RelationshipInformation(sourceList=" + this.getSourceList() + ", relationship=" + this.getRelationship() + ", targetList=" + this.getTargetList() + ")";
    }

    public static class RelationshipInformationBuilder {
        private List<String> sourceList;
        private String relationship;
        private List<String> targetList;

        RelationshipInformationBuilder() {
        }

        public RelationshipInformation.RelationshipInformationBuilder sourceList(List<String> sourceList) {
            this.sourceList = sourceList;
            return this;
        }

        public RelationshipInformation.RelationshipInformationBuilder relationship(String relationship) {
            this.relationship = relationship;
            return this;
        }

        public RelationshipInformation.RelationshipInformationBuilder targetList(List<String> targetList) {
            this.targetList = targetList;
            return this;
        }

        public RelationshipInformation build() {
            return new RelationshipInformation(sourceList, relationship, targetList);
        }

        public String toString() {
            return "RelationshipInformation.RelationshipInformationBuilder(sourceList=" + this.sourceList + ", relationship=" + this.relationship + ", targetList=" + this.targetList + ")";
        }
    }
}
