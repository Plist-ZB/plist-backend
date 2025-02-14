package com.zerobase.plistbackend.module.userplaylist.repository;

import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomUserPlaylistRepository {

  Slice<UserPlaylistResponse> findUserPlaylist (User requestUser, Long cursorId, Pageable pageable);
}