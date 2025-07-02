package com.sensedia.deps.adapter.out;

import com.sensedia.deps.domain.model.Dependency;
import com.sensedia.deps.domain.model.Relation;
import com.sensedia.deps.domain.port.out.DependencyPersistencePort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InMemoryDependencyRepositoryAdapter implements DependencyPersistencePort {

    private final Set<Dependency> dependencies = new HashSet<>();
    private final Set<Relation> relations = new HashSet<>();

    @Override
    public void registerDependency(Dependency dependency) {
        dependencies.add(dependency);
    }

    @Override
    public void registerRelation(Relation relation) {
        relations.add(relation);
    }

    @Override
    public List<Dependency> getAllDependencies() {
        return new ArrayList<>(this.dependencies);
    }

    @Override
    public List<Relation> getAllRelation() {
        return new ArrayList<>(this.relations);
    }

    @Override
    public boolean exists(Dependency dependency) {
        return dependencies.contains(dependency);
    }

    @Override
    public boolean existsRelation(Relation relation) {
        return relations.contains(relation);
    }

    public void printAll() {
        System.out.println("Dependências:");
        dependencies.forEach(d -> System.out.println("- " + d.getName() + " : " + d.getVersion()));

        System.out.println("\nRelações:");
        relations.forEach(r -> System.out.println(r.from().getName() + " -> " + r.to().getName()));
    }
}
