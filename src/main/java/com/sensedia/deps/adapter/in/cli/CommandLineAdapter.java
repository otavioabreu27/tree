package com.sensedia.deps.adapter.in.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.github.lalyos.jfiglet.FigletFont;
import com.sensedia.deps.domain.model.ExportFormat;
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

    public void runInteractive() {
        try (Scanner scanner = new Scanner(System.in)) {
            // ASCII Banner
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/standard.flf");
            String asciiArt = FigletFont.convertOneLine(fontStream, "TheR");
            System.out.println(asciiArt);
            System.out.println("Bem-vindo ao Tree, o gerador de grafo de dependências!\n");

            // Regex opcional
            System.out.print("Deseja aplicar uma regex para filtrar o nome dos projetos? (s/n): ");
            String usarRegex = scanner.nextLine().trim().toLowerCase();

            Optional<String> optionalRegex = Optional.empty();
            if (usarRegex.equals("s")) {
                System.out.print("Digite a regex desejada: ");
                optionalRegex = Optional.of(scanner.nextLine().trim());
            }

            // Caminho dos projetos
            System.out.print("Informe o diretório onde os projetos estão localizados: ");
            String path = scanner.nextLine().trim();

            // Nome do arquivo de saída
            System.out.print("Informe o nome do arquivo de saída (ex: saida_grafo): ");
            String outputFile = scanner.nextLine().trim();
            if (outputFile.isEmpty())
                outputFile = "graph";

            List<String> exportTypes = exportGraphUseCase.listRegisteredExportTypes();
            System.out.println("Escolha um dos tipos de exportação disponíveis: " + String.join(", ", exportTypes));
            String exportFormat = scanner.nextLine().trim().toUpperCase();
            if (!exportTypes.contains(exportFormat)) {
                System.out.println("Formato inválido. Usando HTML por padrão.");
                exportFormat = ExportFormat.HTML.name();
            }

            System.out.println("\n🔍 Diretório alvo: " + path);
            optionalRegex.ifPresent(r -> System.out.println("🔎 Usando filtro regex: " + r));

            // Lógica principal
            System.out.println("📦 Lendo dependências...");
            var dependencies = readDependencyUseCase.readDependencies(path, optionalRegex);
            registerDependencyUseCase.registerDependencies(dependencies);

            System.out.println("🔗 Construindo grafo de dependências...");
            Graph graph = createGraphUseCase.execute();

            System.out.println("📤 Exportando grafo para " + outputFile + "...");
            exportGraphUseCase.export(graph, outputFile, exportFormat);

            System.out.println("✅ Grafo exportado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
