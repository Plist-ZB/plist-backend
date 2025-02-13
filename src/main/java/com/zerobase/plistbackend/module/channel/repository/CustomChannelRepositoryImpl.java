package com.zerobase.plistbackend.module.channel.repository;

import static com.zerobase.plistbackend.module.channel.entity.QChannel.channel;
import static com.zerobase.plistbackend.module.user.entity.QUser.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.user.type.UserErrorStatus;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomChannelRepositoryImpl implements CustomChannelRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Slice<StreamingChannelResponse> findStreamingChannel(Long lastId, Pageable pageable) {

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(channel.channelStatus.eq(ChannelStatus.CHANNEL_STATUS_ACTIVE));
    if (lastId != null) {
      booleanBuilder.and(channel.channelId.lt(lastId));
    }

    List<Channel> channelList = queryFactory.selectFrom(channel).where(booleanBuilder)
        .orderBy(channel.channelId.desc()).limit(
            pageable.getPageSize() + 1).fetch();

    List<StreamingChannelResponse> content = channelList.stream()
        .map(it -> StreamingChannelResponse.createStreamingChannelResponse(it,
            findChannelHostUser(it))).toList();

    boolean hasNext = false;

    if (content.size() > pageable.getPageSize()) {
      content.remove(pageable.getPageSize());
      hasNext = true;
    }

    return new SliceImpl<>(content, pageable, hasNext);

  }

  private User findChannelHostUser(Channel channel) {

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(user.userId.eq(channel.getChannelHostId()));

    return Optional.ofNullable(queryFactory.selectFrom(user).where(booleanBuilder).fetchOne())
        .orElseThrow(() -> new UserException(
            UserErrorStatus.USER_NOT_FOUND));
  }
}
