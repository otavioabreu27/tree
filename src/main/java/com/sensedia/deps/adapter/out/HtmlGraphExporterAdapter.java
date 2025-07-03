package com.sensedia.deps.adapter.out;

import java.util.Set;
import com.sensedia.deps.domain.model.Dependency;
import com.sensedia.deps.domain.model.ExportFormat;
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
    html.append("    html, body { margin: 0; padding: 0; height: 100%; background-color: #1e1e1e; color: #fff; }\n");
    html.append("    svg { width: 100vw; height: 100vh; display: block; }\n");
    html.append("    .link { stroke: #999; stroke-opacity: 0.4; }\n");
    html.append("    text { pointer-events: none; font-size: 10px; fill: #fff; }\n");
    html.append("    circle { fill: #4F2C68; transition: fill 0.2s; color: #fff; }\n");
    html.append("    .highlight { fill: #EA5F14; stroke: #ff6600; stroke-width: 1; }\n");
    html.append("    .dimmed { opacity: 0.2; }\n");
    html.append("    #search, #center {\n");
    html.append("      position: absolute;\n");
    html.append("      top: 10px;\n");
    html.append("      z-index: 10;\n");
    html.append("      padding: 8px 12px;\n");
    html.append("      font-size: 14px;\n");
    html.append("      border: none;\n");
    html.append("      border-radius: 4px;\n");
    html.append("      background-color: #4F2C68;\n");
    html.append("      color: #EA5F14;\n");
    html.append("      font-weight: bold;\n");
    html.append("    }\n");
    html.append("    #search { right: 10px; }\n");
    html.append("    #center { left: 10px; cursor: pointer; }\n");
    html.append("    input#search {\n");
    html.append("      background-color: #fff;\n");
    html.append("      color: #000;\n");
    html.append("      border: 1px solid #ccc;\n");
    html.append("    }\n");
    html.append("  </style>\n");
    html.append("\n</head>\n<body>\n");

    html.append("<input id=\"search\" type=\"text\" placeholder=\"Filtrar nós...\">\n");
    html.append("<button id=\"center\" title=\"Centralizar\">⟳ Centralizar</button>\n");
    html.append("<svg></svg>\n<script>\n");

    html.append("const graph = {\n  nodes: [\n");
    int i = 0;
    for (Dependency d : nodes) {
      String name = d.getName().trim();
      html.append(String.format("    { id: \"%s\" }%s\n", name, ++i < nodes.size() ? "," : ""));
    }

    html.append("  ],\n  links: [\n");
    i = 0;
    for (Relation r : edges) {
      String from = r.from().getName().trim();
      String to = r.to().getName().trim();
      html.append(String.format("    { source: \"%s\", target: \"%s\" }%s\n",
          from, to, ++i < edges.size() ? "," : ""));
    }
    html.append("  ]\n};\n");

    html.append("""
            const svg = d3.select("svg");
            const width = window.innerWidth;
            const height = window.innerHeight;

            const zoom = d3.zoom().on("zoom", (event) => {
                container.attr("transform", event.transform);
            });
            svg.call(zoom);

            const container = svg.append("g");

            const simulation = d3.forceSimulation(graph.nodes)
              .force("link", d3.forceLink(graph.links)
                .id(d => d.id)
                .distance(180)
                .strength(0.2))
              .force("charge", d3.forceManyBody().strength(-1200).distanceMax(500))
              .force("center", d3.forceCenter(width / 2, height / 2));

            const link = container.append("g")
              .attr("class", "links")
              .selectAll("line")
              .data(graph.links)
              .join("line")
              .attr("class", "link")
              .attr("stroke-width", 1.5);

            const node = container.append("g")
              .attr("class", "nodes")
              .selectAll("circle")
              .data(graph.nodes)
              .join("circle")
              .attr("r", 6)
              .attr("fill", "steelblue")
              .attr("class", "node")
              .call(drag(simulation));

            node.append("title").text(d => d.id);

            const text = container.append("g")
              .attr("class", "labels")
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

            const linkedById = {};
            graph.links.forEach(d => {
              linkedById[`${d.source.id},${d.target.id}`] = true;
              linkedById[`${d.target.id},${d.source.id}`] = true;
            });

            function isConnected(a, b) {
              return linkedById[`${a.id},${b.id}`] || a.id === b.id;
            }

            node.on("mouseover", function(event, d) {
              node.classed("dimmed", o => !isConnected(d, o));
              text.classed("dimmed", o => !isConnected(d, o));
              link.classed("dimmed", o => o.source.id !== d.id && o.target.id !== d.id);

              node.classed("highlight", o => isConnected(d, o));
              text.classed("highlight", o => isConnected(d, o));
              link.classed("highlight", o => o.source.id === d.id || o.target.id === d.id);
            }).on("mouseout", function() {
              node.classed("dimmed", false).classed("highlight", false);
              text.classed("dimmed", false).classed("highlight", false);
              link.classed("dimmed", false).classed("highlight", false);
            });

            document.getElementById("search").addEventListener("input", function (e) {
              const term = e.target.value.toLowerCase();

              if (!term) {
                node.classed("dimmed", false).classed("highlight", false);
                text.classed("dimmed", false).classed("highlight", false);
                link.classed("dimmed", false).classed("highlight", false);
                return;
              }

              node.classed("dimmed", d => !d.id.toLowerCase().includes(term));
              text.classed("dimmed", d => !d.id.toLowerCase().includes(term));
              link.classed("dimmed", d =>
                !d.source.id.toLowerCase().includes(term) &&
                !d.target.id.toLowerCase().includes(term)
              );

              node.classed("highlight", d => d.id.toLowerCase().includes(term));
              text.classed("highlight", d => d.id.toLowerCase().includes(term));
              link.classed("highlight", d =>
                d.source.id.toLowerCase().includes(term) ||
                d.target.id.toLowerCase().includes(term)
              );
            });

            document.getElementById("center").addEventListener("click", () => {
              svg.transition().duration(750).call(
                zoom.transform,
                d3.zoomIdentity.translate(width / 2, height / 2).scale(1)
              );
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

  @Override
  public ExportFormat exportType() {
    return ExportFormat.HTML;
  }
}
