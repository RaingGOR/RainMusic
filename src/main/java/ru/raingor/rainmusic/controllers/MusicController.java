package ru.raingor.rainmusic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raingor.rainmusic.services.MusicService;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicController {
    private final MusicService musicService;

    @GetMapping
    public ResponseEntity<?> getMusicPlayer() {
        return musicService.getMusicByPathFromENV();
    }

    @GetMapping("/{audio_path}")
    public String getMusic(@PathVariable String audio_path) {
        return "music";
    }
}
