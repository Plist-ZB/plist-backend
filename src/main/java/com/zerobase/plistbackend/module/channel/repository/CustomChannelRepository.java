package com.zerobase.plistbackend.module.channel.repository;

import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomChannelRepository {

  Slice<StreamingChannelResponse> findStreamingChannel(Long lastId, Pageable pageable);

}
