package com.github.yituhealthcare.arcgraphqlsample.domain;


import com.github.yituhealthcare.arc.graphql.annotation.Graphql;
import com.github.yituhealthcare.arc.graphql.annotation.GraphqlMethod;
import com.github.yituhealthcare.arc.graphql.annotation.GraphqlMutation;
import com.github.yituhealthcare.arc.graphql.annotation.GraphqlQuery;
import com.github.yituhealthcare.arc.graphql.event.DomainEvent;
import com.github.yituhealthcare.arc.graphql.util.GraphqlPayloadUtil;
import com.github.yituhealthcare.arc.mq.Message;
import com.github.yituhealthcare.arc.mq.consumer.Consumer;
import com.github.yituhealthcare.arcgraphqlsample.infrastructure.CommonRepository;
import com.github.yituhealthcare.arcgraphqlsample.input.ProjectInput;
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
            return new User(UUID.randomUUID().toString(), "zhangsan");
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