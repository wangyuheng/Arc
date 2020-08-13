package ai.care.arc.dgraph.repository;

import ai.care.arc.dgraph.util.RDFUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelationshipInformation {

    private List<String> sourceList;
    private String relationship;
    private List<String> targetList;

    public String toRdf() {
        return this.getSourceList().stream()
                .flatMap(s -> this.getTargetList().stream()
                        .map(t -> RDFUtil.wrapperAndJoin(s, this.getRelationship(), t))
                )
                .collect(Collectors.joining("\n"));
    }
}
