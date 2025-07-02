package com.sensedia.deps.adapter.out;

import java.util.Set;

import com.sensedia.deps.domain.model.Dependency;
import com.sensedia.deps.domain.model.Relation;
import com.sensedia.deps.domain.port.out.GraphExporterPort;

public class HtmlGraphExporterAdapter implements GraphExporterPort<String> {

    @Override
    public String export(Set<Dependency> nodes, Set<Relation> edges) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <title>Dependency Graph</title>\n");
        html.append("  <script src=\"https://d3js.org/d3.v7.min.js\"></script>\n");
        html.append("  <style>\n");
        html.append("    html, body { margin: 0; padding: 0; height: 100%; }\n");
        html.append("    svg { width: 100%; height: 100%; display: block; }\n");
        html.append("    .link { stroke: #999; stroke-opacity: 0.6; }\n");
        html.append("    .node text { pointer-events: none; font-size: 10px; }\n");
        html.append("    circle { fill: #1f77b4; }\n");
        html.append("  </style>\n</head>\n<body>\n");
        html.append("<svg></svg>\n<script>\n");

        html.append("const graph = {\n  nodes: [\n");
        int i = 0;
        for (Dependency d : nodes) {
            html.append(String.format("    { id: \"%s\" }%s\n",
                    d.getName(), ++i < nodes.size() ? "," : ""));
        }

        html.append("  ],\n  links: [\n");
        i = 0;
        for (Relation r : edges) {
            html.append(String.format("    { source: \"%s:%s\", target: \"%s:%s\" }%s\n",
                    r.from().getName(), r.from().getVersion(),
                    r.to().getName(), r.to().getVersion(),
                    ++i < edges.size() ? "," : ""));
        }
        html.append("  ]\n};\n");

        html.append("""
            const svg = d3.select("svg");
            const width = window.innerWidth;
            const height = window.innerHeight;

            const simulation = d3.forceSimulation(graph.nodes)
              .force("link", d3.forceLink(graph.links).id(d => d.id))
              .force("charge", d3.forceManyBody().strength(-800))
              .force("center", d3.forceCenter(width / 2, height / 2));

            const link = svg.append("g")
              .attr("stroke", "#999")
              .attr("stroke-opacity", 0.6)
              .selectAll("line")
              .data(graph.links)
              .join("line")
              .attr("stroke-width", 1.5);

            const node = svg.append("g")
              .attr("stroke", "#fff")
              .attr("stroke-width", 1.5)
              .selectAll("circle")
              .data(graph.nodes)
              .join("circle")
              .attr("r", 6)
              .attr("fill", "steelblue")
              .call(drag(simulation));

            node.append("title").text(d => d.id);

            const text = svg.append("g")
              .selectAll("text")
              .data(graph.nodes)
              .join("text")
              .text(d => d.id);

            simulation.on("tick", () => {
              link
                .attr("x1", d => d.source.x)
                .attr("y1", d => d.source.y)
                .attr("x2", d => d.target.x)
                .attr("y2", d => d.target.y);

              node.attr("cx", d => d.x).attr("cy", d => d.y);
              text.attr("x", d => d.x + 8).attr("y", d => d.y);
            });

            function drag(simulation) {
              function dragstarted(event, d) {
                if (!event.active) simulation.alphaTarget(0.3).restart();
                d.fx = d.x;
                d.fy = d.y;
              }

              function dragged(event, d) {
                d.fx = event.x;
                d.fy = event.y;
              }

              function dragended(event, d) {
                if (!event.active) simulation.alphaTarget(0);
                d.fx = null;
                d.fy = null;
              }

              return d3.drag()
                .on("start", dragstarted)
                .on("drag", dragged)
                .on("end", dragended);
            }
        </script>\n</body>\n</html>
        """);

        return html.toString();
    }
}
