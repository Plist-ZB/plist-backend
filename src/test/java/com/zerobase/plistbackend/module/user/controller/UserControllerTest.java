package com.zerobase.plistbackend.module.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.user.dto.response.PlayTimeResponse;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원의 플리방 이력 정보 가져오기 API 테스트")
    void getPlaytime() throws Exception {
        // given
        int year = 2022;
        Long userId = 123L;

        String wholePlayTime = "3시간 0분";
        PlayTimeResponse playTimeResponse = new PlayTimeResponse(wholePlayTime, 50, 0L);

        given(userService.getPlaytime(userId, year)).willReturn(playTimeResponse);

        CustomOAuth2User dummyUser = mock(CustomOAuth2User.class);
        given(dummyUser.findId()).willReturn(userId);

        // when & then
        mockMvc.perform(get("/v3/api/me/playtime")
                        .param("year", String.valueOf(year))
                        .with(authentication(new TestingAuthenticationToken(dummyUser, null, "ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPlayTime").value(wholePlayTime))
                .andExpect(jsonPath("$.totalParticipant").value(50))
                .andExpect(jsonPath("$.totalFollowers").value(0))
                .andDo(print());
    }
}