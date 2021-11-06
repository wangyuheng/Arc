package io.github.wangyuheng.arcfullexample.infrastructure;

import io.github.wangyuheng.arc.dgraph.repository.SimpleDgraphRepository;
import io.github.wangyuheng.arcfullexample.domain.Project;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository extends SimpleDgraphRepository<Project> {

}
