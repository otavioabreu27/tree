package com.sensedia.deps;

import java.util.List;

import com.sensedia.deps.adapter.in.cli.CommandLineAdapter;
import com.sensedia.deps.adapter.out.CsvGraphExporterAdapter;
import com.sensedia.deps.adapter.out.FilesystemPomReaderAdapter;
import com.sensedia.deps.adapter.out.HtmlGraphExporterAdapter;
import com.sensedia.deps.adapter.out.InMemoryDependencyRepositoryAdapter;
import com.sensedia.deps.adapter.out.JsonGraphExporterAdapter;
import com.sensedia.deps.application.service.CreateGraphService;
import com.sensedia.deps.application.service.ExportGraphService;
import com.sensedia.deps.application.service.ReadDependencyService;
import com.sensedia.deps.application.service.RegisterDependencyService;

public class App {
    public static void main(String[] args) {
        var pomReader = new FilesystemPomReaderAdapter();
        var repository = new InMemoryDependencyRepositoryAdapter();
        
        var htmlExporter = new HtmlGraphExporterAdapter();
        var csvExporter = new CsvGraphExporterAdapter();
        var jsonExporter = new JsonGraphExporterAdapter();

        var readDependencyUseCase = new ReadDependencyService(pomReader);
        var registerDependencyUseCase = new RegisterDependencyService(repository);
        var createGraphUseCase = new CreateGraphService(repository);
        var exportGraphUseCase = new ExportGraphService(
            List.of(htmlExporter, csvExporter, jsonExporter)
        );

        var cli = new CommandLineAdapter(
            registerDependencyUseCase,
            readDependencyUseCase,
            createGraphUseCase,
            exportGraphUseCase
        );

        cli.runInteractive();
    }
}