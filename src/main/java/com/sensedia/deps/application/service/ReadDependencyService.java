package com.sensedia.deps.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.ArrayList;

import com.sensedia.deps.domain.model.ProjectWithDependencies;
import com.sensedia.deps.domain.port.in.ReadDependencyUseCase;
import com.sensedia.deps.domain.port.out.PomReaderPort;

public class ReadDependencyService implements ReadDependencyUseCase {

    private final PomReaderPort pomReader;

    public ReadDependencyService(PomReaderPort pomReader) {
        this.pomReader = pomReader;
    }

    @Override
    public List<ProjectWithDependencies> readDependencies(String rootPath) {
        Set<ProjectWithDependencies> allDependencies = new HashSet<>();
        
        try (Stream<Path> paths = Files.walk(Paths.get(rootPath))) {
            paths
                .filter(p -> p.getFileName().toString().equals("pom.xml"))
                .forEach(pom -> {
                    ProjectWithDependencies projectWithDependencies = pomReader.readProject(pom);

                    allDependencies.add(projectWithDependencies);
                });
        } catch (IOException e) {
            throw new RuntimeException("Erro ao percorrer diretórios", e);
        }

        return new ArrayList<>(allDependencies);
    }
}
