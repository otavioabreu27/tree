package com.sensedia.deps.adapter.in.cli;

import com.sensedia.deps.domain.model.Graph;
import com.sensedia.deps.domain.port.in.CreateGraphUseCase;
import com.sensedia.deps.domain.port.in.ExportGraphUseCase;
import com.sensedia.deps.domain.port.in.ReadDependencyUseCase;
import com.sensedia.deps.domain.port.in.RegisterDependencyUseCase;

public class CommandLineAdapter {

    private final RegisterDependencyUseCase registerDependencyUseCase;
    private final ReadDependencyUseCase readDependencyUseCase;
    private final CreateGraphUseCase createGraphUseCase;
    private final ExportGraphUseCase exportGraphUseCase;

    public CommandLineAdapter(
            RegisterDependencyUseCase registerDependencyUseCase,
            ReadDependencyUseCase readDependencyUseCase,
            CreateGraphUseCase createGraphUseCase,
            ExportGraphUseCase exportGraphUseCase) {
        this.registerDependencyUseCase = registerDependencyUseCase;
        this.readDependencyUseCase = readDependencyUseCase;
        this.createGraphUseCase = createGraphUseCase;
        this.exportGraphUseCase = exportGraphUseCase;
    }

    public void run(String path) {
        System.out.println("📦 Lendo dependências de: " + path);
        var dependencies = readDependencyUseCase.readDependencies(path);
        registerDependencyUseCase.registerDependencies(dependencies);

        System.out.println("🔗 Construindo grafo de dependências...");
        Graph graph = createGraphUseCase.execute();

        System.out.println("📤 Exportando grafo...");
        exportGraphUseCase.export(graph);

        System.out.println("✅ Grafo exportado com sucesso.");
    }
}
