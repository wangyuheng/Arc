package io.github.wangyuheng.arcfullsample.infrastructure;

import io.github.wangyuheng.arc.dgraph.repository.SimpleDgraphRepository;
import io.github.wangyuheng.arcfullsample.domain.Milestone;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MilestoneRepository extends SimpleDgraphRepository<Milestone> {

    public List<Milestone> listByProjectId(String projectId) {
        Map<String, String> vars = new HashMap<>();
        vars.put("projectId", projectId);
        return this.queryForList("milestone.listByProjectId", vars);
    }

}
