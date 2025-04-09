package com.github.ivannikolaev.link_guardian.service;

import com.github.ivannikolaev.link_guardian.exception.LinkExpiredException;
import com.github.ivannikolaev.link_guardian.exception.LinkNotFoundException;
import com.github.ivannikolaev.link_guardian.model.entity.ShortLink;
import com.github.ivannikolaev.link_guardian.repository.LinkRepository;
import com.github.ivannikolaev.link_guardian.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LinkService {
    public static final int DEFAULT_CODE_LENGTH = 10;
    private final LinkRepository linkRepository;
    private final CodeGenerator codeGenerator;

    @Transactional
    public ShortLink createShortLink(String originalUrl, int expirationHours, int maxClicks) {
        String shortCode = generateShortCode();
        ShortLink shortLink = ShortLink.builder()
                .originalUrl(originalUrl)
                .shortCode(shortCode)
                .expirationTime(LocalDateTime.now().plusHours(expirationHours))
                .maxClicks(maxClicks)
                .active(true)
                .build();
        return linkRepository.save(shortLink);
    }

    @Transactional
    public String processRedirect(String shortCode) {
        ShortLink shortLink = linkRepository.findByShortCode(shortCode)
                .orElseThrow(LinkNotFoundException::new);
        validateLink(shortLink);
        incrementClicks(shortLink);
        return shortLink.getOriginalUrl();
    }

    private void incrementClicks(ShortLink shortLink) {
        shortLink.setCurrentClicks(shortLink.getCurrentClicks() + 1);
        linkRepository.save(shortLink);
    }

    private void validateLink(ShortLink shortLink) {
        if (!shortLink.isActive()) {
            throw new LinkExpiredException("Link is deactivated");
        }
        if (LocalDateTime.now().isAfter(shortLink.getExpirationTime())) {
            shortLink.setActive(false);
            linkRepository.save(shortLink);
            throw new LinkExpiredException("Link expired");
        }
        if (shortLink.getCurrentClicks() >= shortLink.getMaxClicks()) {
            shortLink.setActive(false);
            linkRepository.save(shortLink);
            throw new LinkExpiredException("Link reached maximum clicks");
        }
    }


    private String generateShortCode() {
        String code;
        do {
            code = codeGenerator.generateCode(DEFAULT_CODE_LENGTH);
        } while (linkRepository.existsByShortCode(code));
        return code;
    }
}
