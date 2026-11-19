package ru.raingor.rainmusic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MusicService {
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

}
