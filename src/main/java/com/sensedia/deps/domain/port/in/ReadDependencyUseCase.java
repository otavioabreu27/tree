package com.sensedia.deps.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.sensedia.deps.domain.model.ProjectWithDependencies;

public interface ReadDependencyUseCase {
    List<ProjectWithDependencies> readDependencies(String rootPath, Optional<String> regexString);
}
