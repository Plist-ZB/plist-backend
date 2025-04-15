package com.zerobase.plistbackend.module.channel.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zerobase.plistbackend.module.channel.dto.response.OtherClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.service.ChannelService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ChannelService channelService;

  @TestConfiguration
  static class MockConfig {

    @Bean
    public ChannelService channelService() {
      return Mockito.mock(ChannelService.class);
    }
  }

  @Test
  @DisplayName("특정 회원의 과거 채널 히스토리를 조회")
  void testFindOtherChannelHistory() throws Exception {
    // given
    Long userId = 1L;
    Long cursorId = null;
    Pageable pageable = PageRequest.of(0, 20);

    OtherClosedChannelResponse response = new OtherClosedChannelResponse(
        2L,
        "string",
        "string",
        "",
        "2025-04-08T00:51:19"
    );

    List<OtherClosedChannelResponse> responseList = List.of(response);
    Slice<OtherClosedChannelResponse> slice = new SliceImpl<>(responseList, pageable, false);

    given(channelService.findOtherUserChannelHistory(cursorId, pageable, userId))
        .willReturn(slice);

    // when & then
    mockMvc.perform(get("/v3/api/user/other/history/{userId}", userId)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].channelId").value(2))
        .andExpect(jsonPath("$.content[0].channelName").value("string"))
        .andExpect(jsonPath("$.content[0].channelCategoryName").value("string"))
        .andExpect(jsonPath("$.content[0].channelThumbnail").value(""))
        .andExpect(jsonPath("$.content[0].channelCreatedAt").value("2025-04-08T00:51:19"))
        .andExpect(jsonPath("$.hasNext").value(false));
  }


}
