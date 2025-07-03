package com.sensedia.deps.adapter.out;

import java.util.Set;

import com.sensedia.deps.domain.model.Dependency;
import com.sensedia.deps.domain.model.ExportFormat;
import com.sensedia.deps.domain.model.Relation;
import com.sensedia.deps.domain.port.out.GraphExporterPort;

public class CsvGraphExporterAdapter implements GraphExporterPort<String> {

    @Override
    public String export(Set<Dependency> nodes, Set<Relation> edges) {
        StringBuilder csv = new StringBuilder();
        
        csv.append("Source,Target\n");

        for (Relation edge : edges) {
            csv.append(String.format("%s,%s\n", edge.from().getName(), edge.to().getName()));
        }
        
        csv.append("# Nodes:\n");
        for (Dependency node : nodes) {
            csv.append(String.format("# %s\n", node.getName()));
        }
        
        return csv.toString();
    }

    @Override
    public ExportFormat exportType() {
        return ExportFormat.CSV;
    }
}
