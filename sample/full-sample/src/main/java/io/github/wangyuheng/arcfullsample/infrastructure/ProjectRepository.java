package io.github.wangyuheng.arcfullsample.infrastructure;

import io.github.wangyuheng.arc.dgraph.repository.SimpleDgraphRepository;
import io.github.wangyuheng.arcfullsample.domain.Project;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository extends SimpleDgraphRepository<Project> {

}
