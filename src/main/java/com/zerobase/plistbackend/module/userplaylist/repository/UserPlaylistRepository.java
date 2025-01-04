package com.zerobase.plistbackend.module.userplaylist.repository;

import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPlaylistRepository extends JpaRepository<UserPlaylist, Long> {

}
