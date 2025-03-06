package com.zerobase.plistbackend.module.channel.repository;

import static com.zerobase.plistbackend.module.category.entity.QCategory.category;
import static com.zerobase.plistbackend.module.channel.entity.QChannel.channel;
import static com.zerobase.plistbackend.module.participant.entity.QParticipant.participant;
import static com.zerobase.plistbackend.module.playlist.entity.QPlaylist.playlist;
import static com.zerobase.plistbackend.module.user.entity.QUser.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.plistbackend.module.channel.dto.response.ClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.user.entity.User;
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

    List<StreamingChannelResponse> content = queryFactory
        .select(Projections.constructor(StreamingChannelResponse.class,
            channel.channelId,
            channel.channelName,
            category.categoryName,
            playlist.videoList,
            channel.channelCreatedAt,
            user.userName,
            participant.countDistinct()))
        .from(channel)
        .leftJoin(category).on(category.categoryId.eq(channel.category.categoryId))
        .leftJoin(playlist).on(playlist.channel.channelId.eq(channel.channelId))
        .leftJoin(participant).on(participant.channel.channelId.eq(channel.channelId))
        .leftJoin(user).on(user.userId.eq(channel.channelHost.userId))
        .where(booleanBuilder)
        .groupBy(channel.channelId)
        .orderBy(channel.channelId.desc())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    boolean hasNext = setHasNext(pageable, content);

    return new SliceImpl<>(content, pageable, hasNext);
  }

  @Override
  public Slice<StreamingChannelResponse> findStreamingChannelOrderByParticipantCount(Long cursorId,
      Long cursorPopular, Pageable pageable) {

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(channel.channelStatus.eq(ChannelStatus.CHANNEL_STATUS_ACTIVE));

    List<StreamingChannelResponse> content = queryFactory
        .select(Projections.constructor(StreamingChannelResponse.class,
            channel.channelId,
            channel.channelName,
            category.categoryName,
            playlist.videoList,
            channel.channelCreatedAt,
            user.userName,
            participant.countDistinct()))
        .from(channel)
        .leftJoin(category).on(category.categoryId.eq(channel.category.categoryId))
        .leftJoin(playlist).on(playlist.channel.channelId.eq(channel.channelId))
        .leftJoin(participant).on(participant.channel.channelId.eq(channel.channelId))
        .leftJoin(user).on(user.userId.eq(channel.channelHost.userId))
        .where(booleanBuilder)
        .groupBy(channel.channelId)
        .having(
            cursorPopular != null && cursorId != null ? participant.countDistinct()
                .lt(cursorPopular) // 커서값이 null이 아닐 때만 조건 추가
                .or(participant.countDistinct().eq(cursorPopular)
                    .and(channel.channelId.lt(cursorId))) : null)
        .orderBy(participant.countDistinct().desc(), channel.channelId.desc())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    boolean hasNext = setHasNext(pageable, content);

    return new SliceImpl<>(content, pageable, hasNext);
  }

  @Override
  public Slice<StreamingChannelResponse> findStreamingChannelFromCategoryIdOrderByParticipantCount(
      Long categoryId, Long cursorId, Long cursorPopular, Pageable pageable) {

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(channel.channelStatus.eq(ChannelStatus.CHANNEL_STATUS_ACTIVE));
    booleanBuilder.and(channel.category.categoryId.eq(categoryId));

    List<StreamingChannelResponse> content = queryFactory
        .select(Projections.constructor(StreamingChannelResponse.class,
            channel.channelId,
            channel.channelName,
            category.categoryName,
            playlist.videoList,
            channel.channelCreatedAt,
            user.userName,
            participant.countDistinct()))
        .from(channel)
        .leftJoin(category).on(category.categoryId.eq(channel.category.categoryId))
        .leftJoin(playlist).on(playlist.channel.channelId.eq(channel.channelId))
        .leftJoin(participant).on(participant.channel.channelId.eq(channel.channelId))
        .leftJoin(user).on(user.userId.eq(channel.channelHost.userId))
        .where(booleanBuilder)
        .groupBy(channel.channelId)
        .having(
            cursorPopular != null && cursorId != null ? participant.countDistinct()
                .lt(cursorPopular) // 커서값이 null이 아닐 때만 조건 추가
                .or(participant.countDistinct().eq(cursorPopular)
                    .and(channel.channelId.lt(cursorId))) : null)
        .orderBy(participant.countDistinct().desc(), channel.channelId.desc())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    boolean hasNext = setHasNext(pageable, content);

    return new SliceImpl<>(content, pageable, hasNext);
  }

  @Override
  public Slice<ClosedChannelResponse> findClosedChannelOrderByChannelId(User requestUser,
      Long cursorId,
      Pageable pageable) {

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(channel.channelHost.userId.eq(user.userId));
    booleanBuilder.and(channel.channelStatus.eq(ChannelStatus.CHANNEL_STATUS_CLOSED));
    if (cursorId != null) {
      booleanBuilder.and(channel.channelId.lt(cursorId));
    }

    List<ClosedChannelResponse> content = queryFactory
        .select(Projections.constructor(ClosedChannelResponse.class,
            channel.channelId,
            channel.channelName,
            category.categoryName,
            playlist.videoList,
            channel.channelCreatedAt,
            channel.channelFinishedAt,
            user.userName,
            channel.channelLastParticipantCount
        ))
        .from(channel)
        .leftJoin(category).on(category.categoryId.eq(channel.category.categoryId))
        .leftJoin(playlist).on(playlist.channel.channelId.eq(channel.channelId))
        .leftJoin(user).on(user.userId.eq(channel.channelHost.userId))
        .where(booleanBuilder)
        .groupBy(channel.channelId)
        .orderBy(channel.channelId.desc())
        .limit(pageable.getPageSize() + 1)
        .fetch();

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
