package io.github.wangyuheng.arcgraphqlexample.domain;


import io.github.wangyuheng.arc.graphql.annotation.Graphql;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlMethod;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlMutation;
import io.github.wangyuheng.arc.graphql.annotation.GraphqlQuery;
import io.github.wangyuheng.arc.graphql.event.DomainEvent;
import io.github.wangyuheng.arc.graphql.util.GraphqlPayloadUtil;
import io.github.wangyuheng.arc.mq.Message;
import io.github.wangyuheng.arc.mq.consumer.Consumer;
import io.github.wangyuheng.arcgraphqlexample.infrastructure.CommonRepository;
import io.github.wangyuheng.arcgraphqlexample.input.ProjectInput;
import graphql.GraphQLContext;
import graphql.schema.DataFetcher;
import org.slf4j.Logger;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Graphql
public class Project {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Project.class);
    private String id;
    private String name;
    private String description;
    private OffsetDateTime createTime;

    private final CommonRepository repository;

    public Project(CommonRepository repository) {
        this.repository = repository;
    }

    @GraphqlMethod(type = "Project")
    public DataFetcher<List<Milestone>> milestones() {
        return dataFetchingEnvironment -> {
            Project project = dataFetchingEnvironment.getSource();
            Milestone.MilestoneStatus milestoneStatus = Milestone.MilestoneStatus.valueOf(dataFetchingEnvironment.getArgument("status"));
            return Arrays.asList(Milestone.builder()
                            .id(UUID.randomUUID().toString())
                            .description("milestone include project " + project.id)
                            .status(milestoneStatus)
                            .name("m1")
                            .build(),
                    Milestone.builder()
                            .id(UUID.randomUUID().toString())
                            .description("milestone include project " + project.id)
                            .status(milestoneStatus)
                            .name("m2")
                            .build());
        };
    }


    @GraphqlMethod(type = "Milestone")
    public DataFetcher<User> creator() {
        return dataFetchingEnvironment -> {
            Milestone milestone = dataFetchingEnvironment.getSource();
            GraphQLContext graphQLContext = dataFetchingEnvironment.getContext();
            Project project = graphQLContext.get("Project");
            return new User(UUID.randomUUID().toString(), "zhangsan", project.name);
        };
    }

    @GraphqlQuery
    public DataFetcher<Project> project() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            return repository.list().stream()
                    .filter(it -> Objects.equals(it.id, id))
                    .findAny()
                    .orElse(null);
        };
    }

    @GraphqlQuery
    public DataFetcher<List<Project>> projects() {
        return dataFetchingEnvironment -> repository.list();
    }

    @GraphqlMutation
    public DataFetcher<Project> createProject() {
        return dataFetchingEnvironment -> {
            ProjectInput input = GraphqlPayloadUtil.resolveArguments(dataFetchingEnvironment.getArguments(), ProjectInput.class);
            OffsetDateTime now = OffsetDateTime.now();

            this.id = UUID.randomUUID().toString();
            this.name = input.getName();
            this.description = input.getDescription();
            this.createTime = now;

            repository.add(this);
            return this;
        };
    }

    @Consumer(topic = "projects")
    public void p1(Message<DomainEvent> record){
        log.info("p1: {}", record);
    }

    @Consumer(topic = "projects", id = "consumerP2")
    public void p12(Message<DomainEvent> record){
        log.info("p2: {}", record);
    }
}