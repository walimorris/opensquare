package com.morris.opensquare.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ImageControllerTest {

    @Autowired
    private MockMvc mockModelViewController;

    @MockBean
    HttpServletRequest httpServletRequest;

    private static final String YOUTUBE_IMAGE_REQUEST = "/images/youtube.png";
    private static final String TWITTER_IMAGE_REQUEST = "/images/twitter.png";
    private static final String VKONTAKTE_IMAGE_REQUEST = "/images/vkontakte.png";
    private static final String OP_IMAGE_REQUEST = "/images/op.png";

    @Test
    void getImage() throws Exception {
        when(httpServletRequest.getRequestURI()).thenReturn(YOUTUBE_IMAGE_REQUEST);
        this.mockModelViewController.perform(get(YOUTUBE_IMAGE_REQUEST)).andExpect(status().isOk());
    }

    @Test
    void getYouTubeImage() throws Exception {
        this.mockModelViewController.perform(get(YOUTUBE_IMAGE_REQUEST)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    void getTwitterImage() throws Exception {
        this.mockModelViewController.perform(get(TWITTER_IMAGE_REQUEST)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    void getVkontakteImage() throws Exception {
        this.mockModelViewController.perform(get(VKONTAKTE_IMAGE_REQUEST)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    void getOPImage() throws Exception {
        this.mockModelViewController.perform(get(OP_IMAGE_REQUEST)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }
}
