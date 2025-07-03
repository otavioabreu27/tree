package com.sensedia.deps.domain.model;

public enum ExportFormat {
    HTML(".html"),
    JSON(".json"),
    CSV(".csv");

    private final String extension;

    ExportFormat(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static ExportFormat fromString(String value) {
        return ExportFormat.valueOf(value.toUpperCase());
    }
}
