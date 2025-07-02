package com.sensedia.deps;

import com.sensedia.deps.adapter.in.cli.CommandLineAdapter;
import com.sensedia.deps.adapter.out.FilesystemPomReaderAdapter;
import com.sensedia.deps.adapter.out.HtmlGraphExporterAdapter;
import com.sensedia.deps.adapter.out.InMemoryDependencyRepositoryAdapter;
import com.sensedia.deps.application.service.CreateGraphService;
import com.sensedia.deps.application.service.ExportGraphService;
import com.sensedia.deps.application.service.ReadDependencyService;
import com.sensedia.deps.application.service.RegisterDependencyService;

public class App {
    public static void main(String[] args) {
        String path = args.length > 0 ? args[0] : ".";

        var pomReader = new FilesystemPomReaderAdapter();
        var repository = new InMemoryDependencyRepositoryAdapter();
        var graphExporter = new HtmlGraphExporterAdapter();

        var readDependencyUseCase = new ReadDependencyService(pomReader);
        var registerDependencyUseCase = new RegisterDependencyService(repository);
        var createGraphUseCase = new CreateGraphService(repository);
        var exportGraphUseCase = new ExportGraphService(graphExporter);

        var cli = new CommandLineAdapter(registerDependencyUseCase, readDependencyUseCase, createGraphUseCase, exportGraphUseCase);
        cli.run(path);
    }
}