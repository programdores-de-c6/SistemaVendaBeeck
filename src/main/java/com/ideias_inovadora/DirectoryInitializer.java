package com.ideias_inovadora;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DirectoryInitializer {

    @Value("${app.upload.directory:./upload}")
    private String uploadDirectory;

    @PostConstruct
    public void initDirectories() {
        try {
            Path uploadPath = Paths.get(uploadDirectory);
            createDirectoryIfNotExists(uploadPath);

            Path filesPath = uploadPath.resolve("files");
            createDirectoryIfNotExists(filesPath);

            System.out.println(
                "Diretórios de upload: "
                + uploadPath.toAbsolutePath()
            );

        } catch (Exception e) {
            throw new RuntimeException(
                "Não foi possível inicializar os diretórios de upload.",
                e
            );
        }
    }

    private void createDirectoryIfNotExists(Path path)
            throws Exception {

        if (!Files.exists(path)) {
            Files.createDirectories(path);

            System.out.println(
                "Diretório criado: "
                + path.toAbsolutePath()
            );
        }
    }
}