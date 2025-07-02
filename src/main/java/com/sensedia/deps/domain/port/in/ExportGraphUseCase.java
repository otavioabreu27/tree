package com.sensedia.deps.domain.port.in;

import com.sensedia.deps.domain.model.Graph;

public interface ExportGraphUseCase {
    void export(Graph graph);
}