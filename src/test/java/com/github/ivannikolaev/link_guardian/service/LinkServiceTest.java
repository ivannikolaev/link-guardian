package com.github.ivannikolaev.link_guardian.service;

import com.github.ivannikolaev.link_guardian.exception.LinkExpiredException;
import com.github.ivannikolaev.link_guardian.model.entity.ShortLink;
import com.github.ivannikolaev.link_guardian.repository.LinkRepository;
import com.github.ivannikolaev.link_guardian.util.CodeGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    @Mock
    private LinkRepository linkRepository;
    @Mock
    private CodeGenerator codeGenerator;
    @InjectMocks
    private LinkService linkService;

    @Test
    void shouldCreateShortLink() {
        //given
        when(codeGenerator.generateCode(LinkService.DEFAULT_CODE_LENGTH)).thenReturn("123456");
        when(linkRepository.existsByShortCode("123456")).thenReturn(false);
        when(linkRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        ShortLink result = linkService.createShortLink("https://google.com", 1, 10);

        //then
        assertThat(result.getShortCode()).isEqualTo("123456");
        assertThat(result.isActive()).isTrue();
        verify(linkRepository).save(any(ShortLink.class));
    }

    @Test
    void shouldThrowWhenLinkExpired() {
        //given
        ShortLink expiredLink = ShortLink.builder()
                .shortCode("expired")
                .expirationTime(LocalDateTime.now().minusHours(1))
                .active(true)
                .build();

        when(linkRepository.findByShortCode("expired")).thenReturn(Optional.ofNullable(expiredLink));

        //when & then
        assertThatThrownBy(() -> linkService.processRedirect("expired"))
                .isInstanceOf(LinkExpiredException.class)
                .hasMessage("Link expired");
        verify(linkRepository).save(expiredLink);
    }

}