package com.sensedia.deps.application.service;

import java.util.List;

import com.sensedia.deps.domain.model.Dependency;
import com.sensedia.deps.domain.model.ProjectWithDependencies;
import com.sensedia.deps.domain.model.Relation;
import com.sensedia.deps.domain.port.in.RegisterDependencyUseCase;
import com.sensedia.deps.domain.port.out.DependencyPersistencePort;

public class RegisterDependencyService implements RegisterDependencyUseCase {

    private final DependencyPersistencePort persistence;

    public RegisterDependencyService(DependencyPersistencePort persistence) {
        this.persistence = persistence;
    }

    @Override
    public void registerDependencies(List<ProjectWithDependencies> projects) {
        for (ProjectWithDependencies projectWithDeps : projects) {
            Dependency project = projectWithDeps.project();
            if (project == null) continue;

            if (!persistence.exists(project)) {
                persistence.registerDependency(project);
            }

            for (Dependency dependency : projectWithDeps.dependencies()) {
                if (!persistence.exists(dependency)) {
                    persistence.registerDependency(dependency);
                }

                Relation relation = new Relation(project, dependency);
                if (!persistence.existsRelation(relation)) {
                    persistence.registerRelation(relation);
                }
            }
        }
    }
}
