package com.sensedia.deps.domain.model;

import java.util.Set;

public record Graph(Set<Dependency> nodes, Set<Relation> edges) {}
