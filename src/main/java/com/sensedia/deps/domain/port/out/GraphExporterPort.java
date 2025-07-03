package com.sensedia.deps.domain.port.out;

import java.util.Set;

import com.sensedia.deps.domain.model.Dependency;
import com.sensedia.deps.domain.model.ExportFormat;
import com.sensedia.deps.domain.model.Relation;

public interface GraphExporterPort<T> {
    T export(Set<Dependency> nodes, Set<Relation> edges);
    ExportFormat exportType();
}