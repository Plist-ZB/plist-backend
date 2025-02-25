package com.zerobase.plistbackend.module.channel.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.repository.ChannelRepository;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.home.model.Video;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.playlist.domain.PlaylistCrudEvent;
import com.zerobase.plistbackend.module.playlist.entity.Playlist;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class ChannelServiceImplTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @InjectMocks
  private ChannelServiceImpl channelService;


  @Test
  @DisplayName("비디오 추가시 이벤트 객체 생성")
  public void success_addVideoToChannel() {
    Long channelId = 1L;
    VideoRequest videoRequest = new VideoRequest("Test VideoId", "Test VideoName", "Video.img");

    Channel channel = Channel.builder()
        .channelId(1L)
        .channelStatus(ChannelStatus.CHANNEL_STATUS_ACTIVE)
        .channelPlaylist(new Playlist())
        .build();

    when(channelRepository.findByIdWithLock(channelId)).thenReturn(Optional.of(channel));

    Participant participant = Participant.builder()
        .participantId(1L)
        .channel(channel)
        .build();

    User user = User.builder()
        .userId(1L)
        .userEmail("TestUser@test.com")
        .userName("TestUser")
        .participant(participant)
        .build();
    when(userRepository.findByUserEmail(anyString())).thenReturn(user);


    CustomOAuth2User oAuth2User = mock(CustomOAuth2User.class);
    when(oAuth2User.findEmail()).thenReturn("TestUser@test.com");

    // when
    channelService.addVideoToChannel(channelId, videoRequest, oAuth2User);

    // then
    verify(eventPublisher, times(1)).publishEvent(any(PlaylistCrudEvent.class));
  }
  @Test
  @DisplayName("비디오 업데이트시 이벤트 객체 생성")
  public void success_updateVideoToChannel() {
    Long channelId = 1L;

    User user = User.builder()
        .userId(1L)
        .userEmail("TestUser@test.com")
        .userName("TestUser")
        .build();

    Channel channel = Channel.builder()
        .channelId(channelId)
        .channelStatus(ChannelStatus.CHANNEL_STATUS_ACTIVE)
        .channelPlaylist(new Playlist())
        .channelParticipants(new ArrayList<>())
        .channelHost(user)
        .build();

    Participant participant = Participant.builder()
        .participantId(1L)
        .user(user)
        .channel(channel)
        .build();

    channel.getChannelParticipants().add(participant);
    user.setParticipant(participant);

    when(channelRepository.findByIdWithLock(channelId)).thenReturn(Optional.of(channel));

    when(userRepository.findByUserEmail(anyString())).thenReturn(user);

    CustomOAuth2User oAuth2User = mock(CustomOAuth2User.class);
    when(oAuth2User.findEmail()).thenReturn("TestUser@test.com");

    String updateChannelPlaylistJson =
        """
                [
                  {
                    "id": 1,
                    "videoName": "Test Video Name",
                    "videoThumbnail": "https://example.com/thumbnail.jpg",
                    "videoId": "abc123xyz"
                  }
                ]
            """;
    // when
    channelService.updateChannelPlaylist(channelId, updateChannelPlaylistJson, oAuth2User);

    // then
    verify(eventPublisher, times(1)).publishEvent(any(PlaylistCrudEvent.class));
  }
  @Test
  @DisplayName("비디오 삭제시 이벤트 객체 생성")
  public void success_deleteVideoToChannel() {
    User user = User.builder()
        .userId(1L)
        .userEmail("TestUser@test.com")
        .userName("TestUser")
        .build();
    when(userRepository.findByUserEmail("TestUser@test.com")).thenReturn(user);

    Video video = Video.builder()
        .id(1L)
        .videoName("Test Video")
        .videoThumbnail("https://example.com/thumbnail.jpg")
        .videoId("abc123xyz")
        .build();

    Playlist playlist = new Playlist();
    playlist.setVideoList(new ArrayList<>());
    playlist.getVideoList().add(video);


    Channel channel = Channel.builder()
        .channelId(1L)
        .channelStatus(ChannelStatus.CHANNEL_STATUS_ACTIVE)
        .channelPlaylist(playlist)
        .channelHost(user)
        .build();
    when(channelRepository.findByIdWithLock(channel.getChannelId())).thenReturn(Optional.of(channel));

    CustomOAuth2User oAuth2User = mock(CustomOAuth2User.class);
    when(oAuth2User.findEmail()).thenReturn("TestUser@test.com");

    // when
    channelService.deleteVideoToChannel(channel.getChannelId(), video.getId(), oAuth2User);

    // then
    verify(eventPublisher, times(1)).publishEvent(any(PlaylistCrudEvent.class));
  }
}