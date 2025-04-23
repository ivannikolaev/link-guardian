package com.github.ivannikolaev.link_guardian.controller;

import com.github.ivannikolaev.link_guardian.exception.LinkExpiredException;
import com.github.ivannikolaev.link_guardian.exception.LinkNotFoundException;
import com.github.ivannikolaev.link_guardian.model.dto.request.CreateLinkRequest;
import com.github.ivannikolaev.link_guardian.model.dto.response.LinkResponse;
import com.github.ivannikolaev.link_guardian.model.dto.response.LinkStatsResponse;
import com.github.ivannikolaev.link_guardian.model.entity.ShortLink;
import com.github.ivannikolaev.link_guardian.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping
    public ResponseEntity<LinkResponse> createShortLink(@RequestBody CreateLinkRequest request) {
        ShortLink shortLink = linkService.createShortLink(request.originalUrl(), request.maxClicks(), request.expirationHours());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{shortCode}")
                .buildAndExpand(shortLink.getShortCode())
                .toUri();
        return ResponseEntity.created(location)
                .body(new LinkResponse(shortLink.getShortCode(),
                        shortLink.getOriginalUrl(),
                        shortLink.getExpirationTime(),
                        shortLink.getMaxClicks()));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectLink(@PathVariable String shortCode) {
        String originalUrl = linkService.processRedirect(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<LinkStatsResponse> getStats(@PathVariable String shortCode) {
        //todo
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ErrorResponse handleLinkNotFound(LinkNotFoundException e) {
        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND, e.getMessage()).build();
    }

    @ExceptionHandler(LinkExpiredException.class)
    public ErrorResponse handleLinkExpired(LinkExpiredException e) {
        return ErrorResponse.builder(e, HttpStatus.GONE, e.getMessage()).build();
    }
}
