package com.zerobase.plistbackend.module.channel.repository;

import com.zerobase.plistbackend.module.channel.dto.response.ClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import com.zerobase.plistbackend.module.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomChannelRepository {

  Slice<StreamingChannelResponse> findStreamingChannelOrderByChannelId(Long cursorId,
      Pageable pageable);

  Slice<StreamingChannelResponse> findStreamingChannelOrderByParticipantCount(Long cursorId,
      Long cursorPopular, Pageable pageable);

  Slice<StreamingChannelResponse> findStreamingChannelFromCategoryIdOrderByParticipantCount(
      Long categoryId, Long cursorId, Long cursorPopular, Pageable pageable);

  Slice<ClosedChannelResponse> findClosedChannelOrderByChannelId(User requestUser, Long cursorId,
      Pageable pageable);
}
