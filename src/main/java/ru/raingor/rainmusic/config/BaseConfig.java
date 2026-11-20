package ru.raingor.rainmusic.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.raingor.rainmusic.domain.LibraryLocation;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.isDirectory;

@Configuration
public class BaseConfig {

    @Bean
    ApplicationRunner initializeApp(Environment env) {
        return args -> {
            // проверяем наличие папки приложения
            Path dir = libraryLocation(env).file();
            if (!isDirectory(dir)) {
                createDirectories(dir);
            }

            // поиск library.json
            Path inventory = dir.resolve("library.json");
            try {
                Files.writeString(inventory, "[]", StandardOpenOption.CREATE_NEW);
            } catch (FileAlreadyExistsException e) {
                if (!Files.isRegularFile(inventory)) {
                    throw new IOException("Ожидался файл по пути " + inventory, e);
                }
            }
        };
    }

    @Bean
    public LibraryLocation libraryLocation(Environment environment) {
        String directory = environment.getProperty("RAINMUSIC_LIBRARY_PATH");
        if (directory == null || directory.isBlank()) {
            directory = System.getenv("LOCALAPPDATA");
            if (directory != null && !directory.isBlank()) {
                directory += "/RainMusic";
            } else {
                throw new IllegalStateException("RAINMUSIC_LIBRARY_PATH is not set and LOCALAPPDATA not found");
            }
        }

        return new LibraryLocation(Path.of(directory));
    }
}
