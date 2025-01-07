package com.zerobase.plistbackend.module.playlist.repository;

import com.zerobase.plistbackend.module.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

}
