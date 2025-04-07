package com.github.ivannikolaev.link_guardian.controller;

import com.github.ivannikolaev.link_guardian.model.dto.request.CreateLinkRequest;
import com.github.ivannikolaev.link_guardian.model.dto.response.LinkResponse;
import com.github.ivannikolaev.link_guardian.model.dto.response.LinkStatsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/links")
public class LinkController {
    @PostMapping
    public ResponseEntity<LinkResponse> createShortLink(@RequestBody CreateLinkRequest request) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectLink(@PathVariable String shortCode) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<LinkStatsResponse> getStats(@PathVariable String shortCode) {
        return ResponseEntity.ok().build();
    }
}
