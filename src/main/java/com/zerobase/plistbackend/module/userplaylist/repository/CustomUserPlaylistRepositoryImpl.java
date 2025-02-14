package com.zerobase.plistbackend.module.userplaylist.repository;

import static com.zerobase.plistbackend.module.userplaylist.entity.QUserPlaylist.userPlaylist;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomUserPlaylistRepositoryImpl implements CustomUserPlaylistRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Slice<UserPlaylistResponse> findUserPlaylist(User requestUser, Long cursorId,
      Pageable pageable) {

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(userPlaylist.user.userId.eq(requestUser.getUserId()));
    if (cursorId != null) {
      booleanBuilder.and(userPlaylist.userPlaylistId.gt(cursorId));
    }

    List<UserPlaylist> userPlaylists = queryFactory.selectFrom(userPlaylist)
        .where(booleanBuilder)
        .orderBy(userPlaylist.userPlaylistId.asc())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    List<UserPlaylistResponse> content = new ArrayList<>(userPlaylists.stream()
        .map(UserPlaylistResponse::fromEntity).toList());

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