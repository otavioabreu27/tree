package com.sensedia.deps.domain.port.out;

import java.util.List;

import com.sensedia.deps.domain.model.Dependency;
import com.sensedia.deps.domain.model.Relation;

public interface DependencyPersistencePort {
    void registerDependency(Dependency dependency);
    void registerRelation(Relation relation);
    List<Dependency> getAllDependencies();
    List<Relation> getAllRelation();
    boolean exists(Dependency dependency);
    boolean existsRelation(Relation relation);
}