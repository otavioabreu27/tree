package com.sensedia.deps.domain.port.in;

import java.util.List;

import com.sensedia.deps.domain.model.ProjectWithDependencies;

public interface RegisterDependencyUseCase {
    void registerDependencies(List<ProjectWithDependencies> projects);
}
