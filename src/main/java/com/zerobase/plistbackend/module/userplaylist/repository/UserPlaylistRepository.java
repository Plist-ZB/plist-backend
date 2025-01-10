package com.zerobase.plistbackend.module.userplaylist.repository;

import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPlaylistRepository extends JpaRepository<UserPlaylist, Long> {

  Optional<UserPlaylist> findByUserAndUserPlaylistId(User user, Long userPlaylistId);

  List<UserPlaylist> findByUser(User user);

  void deleteByUserAndUserPlaylistId(User user, Long userPlaylistId);

  boolean existsByUserAndUserPlaylistId(User user, Long userPlaylistId);

  boolean existsByUserAndUserPlaylistName(User user, String userPlaylistName);
}
