package ru.raingor.rainmusic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.raingor.rainmusic.services.MusicService;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicController {
    private final MusicService musicService;

    @GetMapping
    public ResponseEntity<String[]> getMusicPlayer() {
        return new ResponseEntity<>(musicService.getAllMusic(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String[]> syncLibrary() {
        musicService.syncLibrary();
        return getMusicPlayer();
    }

    @GetMapping("/{audio_path}")
    public String getMusic(@PathVariable String audio_path) {
        return "music";
    }
}
