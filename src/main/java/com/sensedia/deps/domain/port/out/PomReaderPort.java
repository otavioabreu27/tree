package com.sensedia.deps.domain.port.out;

import java.nio.file.Path;
import java.util.Optional;

import com.sensedia.deps.domain.model.ProjectWithDependencies;

public interface PomReaderPort {
    ProjectWithDependencies readProject(Path pomPath, Optional<String> regexString);
}