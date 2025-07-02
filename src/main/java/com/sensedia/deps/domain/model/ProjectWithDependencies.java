package com.sensedia.deps.domain.model;

import java.util.List;

public record ProjectWithDependencies(Dependency project, List<Dependency> dependencies) {
    
}
