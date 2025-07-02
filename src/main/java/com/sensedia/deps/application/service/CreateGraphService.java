package com.sensedia.deps.application.service;

import java.util.HashSet;
import java.util.List;

import com.sensedia.deps.domain.model.Dependency;
import com.sensedia.deps.domain.model.Graph;
import com.sensedia.deps.domain.model.Relation;
import com.sensedia.deps.domain.port.in.CreateGraphUseCase;
import com.sensedia.deps.domain.port.out.DependencyPersistencePort;

public class CreateGraphService implements CreateGraphUseCase {
    private final DependencyPersistencePort persistence;

    public CreateGraphService(DependencyPersistencePort persistence) {
        this.persistence = persistence;
    }

    @Override
    public Graph execute() {
        List<Dependency> nodes = persistence.getAllDependencies();
        List<Relation> edges = persistence.getAllRelation();

        return new Graph(new HashSet<Dependency>(nodes), new HashSet<>(edges));
    }
    
}
