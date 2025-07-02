package com.sensedia.deps.adapter.out;

import java.util.Set;

import com.sensedia.deps.domain.model.Dependency;
import com.sensedia.deps.domain.model.Relation;
import com.sensedia.deps.domain.port.out.GraphExporterPort;

public class JsonGraphExporterAdapter implements GraphExporterPort<String> {

    @Override
    public String export(Set<Dependency> nodes, Set<Relation> edges) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"nodes\": [\n");

        int i = 0;
        for (Dependency d : nodes) {
            sb.append("    { \"id\": \"" + d.getName() + "\", \"version\": \"" + d.getVersion() + "\" }");
            if (++i < nodes.size())
                sb.append(",");
            sb.append("\n");
        }

        sb.append("  ],\n  \"links\": [\n");

        i = 0;
        for (Relation r : edges) {
            sb.append("    { \"source\": \"" + r.from().getName() + "\", \"target\": \"" + r.to().getName() + "\" }");
            if (++i < edges.size())
                sb.append(",");
            sb.append("\n");
        }

        sb.append("  ]\n}");
        return sb.toString();
    }
}
