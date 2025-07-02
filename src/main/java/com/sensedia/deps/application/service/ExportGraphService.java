package com.sensedia.deps.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.sensedia.deps.domain.model.Graph;
import com.sensedia.deps.domain.port.in.ExportGraphUseCase;
import com.sensedia.deps.domain.port.out.GraphExporterPort;

public class ExportGraphService implements ExportGraphUseCase {

    private final GraphExporterPort<String> exporter;

    public ExportGraphService(GraphExporterPort<String> exporter) {
        this.exporter = exporter;
    }

    @Override
    public void export(Graph graph) {
        String html = exporter.export(graph.nodes(), graph.edges());

        Path path = Path.of("./dependency.html");
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, html);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar HTML no disco", e);
        }
    }
}
