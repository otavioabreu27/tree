package com.sensedia.deps.domain.port.in;

import java.util.List;

import com.sensedia.deps.domain.model.Graph;

public interface ExportGraphUseCase {
    void export(Graph graph, String filename, String exportType);
    List<String> listRegisteredExportTypes();
}