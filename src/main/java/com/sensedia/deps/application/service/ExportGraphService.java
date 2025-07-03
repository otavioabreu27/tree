package com.sensedia.deps.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sensedia.deps.domain.model.Graph;
import com.sensedia.deps.domain.model.ExportFormat;
import com.sensedia.deps.domain.port.in.ExportGraphUseCase;
import com.sensedia.deps.domain.port.out.GraphExporterPort;

public class ExportGraphService implements ExportGraphUseCase {

    private final Map<ExportFormat, GraphExporterPort<String>> exporterMap;

    public ExportGraphService(List<GraphExporterPort<String>> exporters) {
        this.exporterMap = new HashMap<>();
        for (GraphExporterPort<String> exporter : exporters) {
            exporterMap.put(exporter.exportType(), exporter);
        }
    }

    @Override
    public void export(Graph graph, String filename, String exportTypeStr) {
        ExportFormat exportType;
        try {
            exportType = ExportFormat.fromString(exportTypeStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de exportação inválido: " + exportTypeStr);
        }

        GraphExporterPort<String> exporter = exporterMap.get(exportType);
        if (exporter == null) {
            throw new RuntimeException("Nenhum exportador registrado para o tipo: " + exportType);
        }

        String file = exporter.export(graph.nodes(), graph.edges());

        if (!filename.endsWith(exportType.getExtension())) {
            filename += exportType.getExtension();
        }

        if (!filename.startsWith("/")) {
            filename = "./" + filename;
        }

        Path path = Path.of(filename);
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, file);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo no disco", e);
        }
    }

    @Override
    public List<String> listRegisteredExportTypes() {
        return exporterMap.keySet().stream()
                .map(Enum::name)
                .toList();
    }
}
