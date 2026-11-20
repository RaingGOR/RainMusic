package ru.raingor.rainmusic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.raingor.rainmusic.config.BaseConfig;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MusicService {
    private final Environment env;
    private final ObjectMapper objectMapper;
    private final BaseConfig baseConfig;

    private static final Set<String> MUSIC_EXTENSIONS = Set.of("mp3", "wav", "mp4", "ogg", "m4a", "aac",
            "oga", "opus", "flac");

    public String[] getAllMusic() {
        Path dir = baseConfig.libraryLocation(env).file();
        Path libraryPath = dir.resolve("library.json");
        try {
            String availableMusic = Files.readString(libraryPath);

            return objectMapper.readValue(availableMusic, String[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Deprecated
    public ResponseEntity<?> getMusicByPathFromENV() {
        try {
            String musicPathFromENV = System.getenv("RAINMUSIC_MUSIC_PATH");
            var music = new FileSystemResource(musicPathFromENV);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .body(music);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void syncLibrary() {
        try {
            Path dir = baseConfig.libraryLocation(env).file();
            Path libraryPath = dir.resolve("library.json");

            var entries = Files.list(dir)
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> {
                        int dot = name.lastIndexOf('.');
                        return dot > 0
                                && MUSIC_EXTENSIONS.contains(name.substring(dot + 1).toLowerCase(Locale.ROOT));
                    })
                    .collect(Collectors.toSet());

            String json = objectMapper.writeValueAsString(entries);

            Files.writeString(libraryPath, json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
