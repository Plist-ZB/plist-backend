package com.zerobase.plistbackend.module.channel.repository;

import static com.zerobase.plistbackend.module.channel.entity.QChannel.channel;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.plistbackend.module.channel.dto.response.ClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.user.entity.User;
import java.util.ArrayList;
import java.util.List;
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
  public Slice<StreamingChannelResponse> findStreamingChannelOrderByChannelId(Long cursorId,
      Pageable pageable) {

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(channel.channelStatus.eq(ChannelStatus.CHANNEL_STATUS_ACTIVE));
    if (cursorId != null) {
      booleanBuilder.and(channel.channelId.lt(cursorId));
    }

    List<Channel> channelList = queryFactory.selectFrom(channel)
        .where(booleanBuilder)
        .orderBy(channel.channelId.desc())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    List<StreamingChannelResponse> content = new ArrayList<>(channelList.stream()
        .map(StreamingChannelResponse::createStreamingChannelResponse).toList());

    boolean hasNext = setHasNext(pageable, content);

    return new SliceImpl<>(content, pageable, hasNext);
  }

  @Override
  public Slice<StreamingChannelResponse> findStreamingChannelOrderByParticipantCount(Long cursorId,
      Long cursorPopular, Pageable pageable) {

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(channel.channelStatus.eq(ChannelStatus.CHANNEL_STATUS_ACTIVE));
    if (cursorId != null && cursorPopular != null) {
      booleanBuilder.and(channel.channelParticipants.size().lt(cursorPopular)
          .or(channel.channelParticipants.size().eq(
              Math.toIntExact(cursorPopular)).and(channel.channelId.lt(cursorId))));
    }

    List<Channel> channelList = queryFactory.selectFrom(channel)
        .where(booleanBuilder)
        .orderBy(channel.channelParticipants.size().desc(), channel.channelId.desc())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    List<StreamingChannelResponse> content = new ArrayList<>(channelList.stream()
        .map(StreamingChannelResponse::createStreamingChannelResponse).toList());

    boolean hasNext = setHasNext(pageable, content);

    return new SliceImpl<>(content, pageable, hasNext);
  }

  @Override
  public Slice<StreamingChannelResponse> findStreamingChannelFromCategoryIdOrderByParticipantCount(
      Long categoryId, Long cursorId, Long cursorPopular, Pageable pageable) {

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(channel.channelStatus.eq(ChannelStatus.CHANNEL_STATUS_ACTIVE));
    booleanBuilder.and(channel.category.categoryId.eq(categoryId));
    if (cursorId != null && cursorPopular != null) {
      booleanBuilder.and(channel.channelParticipants.size().lt(cursorPopular)
          .or(channel.channelParticipants.size().eq(
              Math.toIntExact(cursorPopular)).and(channel.channelId.lt(cursorId))));
    }

    List<Channel> channelList = queryFactory.selectFrom(channel)
        .where(booleanBuilder)
        .orderBy(channel.channelParticipants.size().desc(), channel.channelId.desc())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    List<StreamingChannelResponse> content = new ArrayList<>(channelList.stream()
        .map(StreamingChannelResponse::createStreamingChannelResponse).toList());

    boolean hasNext = setHasNext(pageable, content);

    return new SliceImpl<>(content, pageable, hasNext);
  }

  @Override
  public Slice<ClosedChannelResponse> findClosedChannelOrderByChannelId(User requestUser,
      Long cursorId,
      Pageable pageable) {

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(channel.channelHost.eq(requestUser));
    booleanBuilder.and(channel.channelStatus.eq(ChannelStatus.CHANNEL_STATUS_CLOSED));
    if (cursorId != null) {
      booleanBuilder.and(channel.channelId.lt(cursorId));
    }

    List<Channel> channelList = queryFactory.selectFrom(channel)
        .where(booleanBuilder)
        .orderBy(channel.channelId.desc())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    List<ClosedChannelResponse> content = new ArrayList<>(channelList.stream()
        .map(it -> ClosedChannelResponse.createClosedChannelResponse(it, requestUser)).toList());

    boolean hasNext = setHasNext(pageable, content);

    return new SliceImpl<>(content, pageable, hasNext);
  }

  private <T> boolean setHasNext(Pageable pageable, List<T> content) {
    boolean hasNext = false;

    if (content.size() > pageable.getPageSize()) {
      content.remove(pageable.getPageSize());
      hasNext = true;
    }
    return hasNext;
  }
}
