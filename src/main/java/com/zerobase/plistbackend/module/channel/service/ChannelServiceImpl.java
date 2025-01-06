package com.zerobase.plistbackend.module.channel.service;

import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ChannelResponse;
import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.repository.ChannelRepository;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.participant.dto.response.ParticipantResponse;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.participant.repository.ParticipantRepository;
import com.zerobase.plistbackend.module.playlist.dto.response.PlaylistResponse;
import com.zerobase.plistbackend.module.playlist.entity.Playlist;
import com.zerobase.plistbackend.module.playlist.repository.PlaylistRepository;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import com.zerobase.plistbackend.module.userplaylist.dto.response.UserPlaylistResponse;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import com.zerobase.plistbackend.module.userplaylist.model.Video;
import com.zerobase.plistbackend.module.userplaylist.repository.UserPlaylistRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

  // 웹 페이지에서 재생가능한 동영상만 검색
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ParticipantRepository participantRepository;
  private final UserPlaylistRepository userPlaylistRepository;
  private final PlaylistRepository playlistRepository;

  @Override
  @Transactional
  public ChannelResponse addChannel(CustomOAuth2User customOAuth2User,
      ChannelRequest channelRequest) {
    // TODO: 카테고리 생성 후 카테고리 요청 받는 작업 // 채널을 이미 생성한 경우, 채널을 생성할 수 없게 하는 로직.

    //1. 현재 로그인 되어 있는 사용자 찾기
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    //1-1. 현재 로그인 되어 있는 User가 생성한 방이 있으면 예외 발생.
    List<Participant> userParticipantList = participantRepository.findByUser(user);
    if (userParticipantList.stream().anyMatch(it-> it.getIsHost().equals(true))) {
      throw new RuntimeException("이미 생성한 방이 존재합니다."); // TODO: 예외처리
    }
    //2. 사용자의 userplaylist id값으로 Playlist 객체 생성 (없으면 빈 객체 생성)
    Playlist playlist = new Playlist();
    if (channelRequest.getUserPlaylistId() != null) {
      UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistId(user,
          channelRequest.getUserPlaylistId());
      playlist = Playlist.from(userPlaylist);
    } else {
      playlist.setVideoList(new ArrayList<>());
    }
    //3. 두개를 이용해서 채널 생성
    Channel channel = Channel.createChannel(channelRequest, playlist);
    //3-1. Playlist에 채널 추가.
    playlist.setChannel(channel);
    //4. 채널의 참가자에 User를 변환한 Participant 추가 (HOST)
    Participant participant = Participant.host(user, channel);
    channel.getChannelParticipants().add(participant);
    //5. 채널 저장 // 참가자 저장? // 플레이리스트 저장?
    participantRepository.save(participant);
    playlistRepository.save(playlist);
    channelRepository.save(channel);
    //6. 채널을 DTO로 변환해서 리턴시킴.
    List<ParticipantResponse> participantResponseList = channel.getChannelParticipants().stream()
        .map(
            ParticipantResponse::createParticipantResponse).toList();
    PlaylistResponse playlistResponse = PlaylistResponse.from(playlist);

    return ChannelResponse.createChannelResponse(channel, playlistResponse,
        participantResponseList);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelResponse> findChannelList() {
    List<Channel> channelList = channelRepository.findAllByChannelStatus(
        ChannelStatus.CHANNEL_STATUS_ACTIVE);

    return ChannelResponse.createChannelResponseList(channelList);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelResponse> findChannelFromChannelName(String channelName) {
    List<Channel> channelList = channelRepository.findByChannelStatusAndChannelNameContaining(
        ChannelStatus.CHANNEL_STATUS_ACTIVE,
        channelName);

    return ChannelResponse.createChannelResponseList(channelList);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelResponse> findChannelFromChannelCategory(String channelCategory) {
    List<Channel> channelList = channelRepository.findByChannelStatusAndChannelCategory(
        ChannelStatus.CHANNEL_STATUS_ACTIVE,
        channelCategory);

    return ChannelResponse.createChannelResponseList(channelList);
  }


  @Override
  @Transactional
  public void enterChannel(CustomOAuth2User customOAuth2User, Long channelId) {
    // TODO: 채널의 정원이 가득 찼을 경우, 채널에 참가하지 못하게 하는 로직 필요.
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new RuntimeException("해당 채널이 없습니다.")); // TODO: 예외처리

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    if (channel.getChannelParticipants().stream().map(it -> it.getUser().getUserEmail())
        .noneMatch(it -> it.equals(user.getUserEmail()))) {
      Participant participant = Participant.viewer(user, channel);
      channel.getChannelParticipants().add(participant);
      participantRepository.save(participant);
      channelRepository.save(channel);
    } else {
      throw new RuntimeException("이미 채널에 참여하고 있습니다."); // TODO: 예외처리
    }
  }

  @Override
  @Transactional
  public void userExitChannel(CustomOAuth2User customOAuth2User, Long channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new RuntimeException("해당 채널이 없습니다.")); // TODO: 예외처리

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    if (channel.getChannelParticipants().stream().map(it -> it.getUser().getUserEmail())
        .anyMatch(it -> it.equals(user.getUserEmail()))) {
      participantRepository.deleteByUser(user);
    } else {
      throw new RuntimeException("해당 채널에 참여하고 있지 않습니다."); // TODO: 예외처리
    }
  } // TODO: 테스트코드를 활용해 테스트 필요.

  @Override
  @Transactional
  public void hostExitChannel(CustomOAuth2User customOAuth2User, Long channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new RuntimeException("해당 채널이 없습니다.")); // TODO: 예외처리

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    if (channel.getChannelParticipants().stream().filter(it -> it.getIsHost().equals(true))
        .anyMatch(it -> it.getUser().getUserEmail().equals(user.getUserEmail()))) {
      participantRepository.deleteByChannel(channel);
      Channel.closeChannel(channel);
      channelRepository.save(channel);
    } else {
      throw new RuntimeException("호스트가 아닌 사용자는 채널을 닫을 수 없습니다."); // TODO: 예외처리
    }
  }

  @Override
  @Transactional
  public ChannelResponse addVideoToChannel(Long channelId, VideoRequest videoRequest,
      CustomOAuth2User customOAuth2User) {
    //1. 로그인 한 유저 정보 가져오기.
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    //2. 채널 Id를 통해 해당 채널 검색.
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new RuntimeException("해당 채널이 없습니다.")); // TODO: 예외처리
    //3. 유저가 해당 채널의 참여자인지 검증.
    if (channel.getChannelParticipants().stream().noneMatch(it -> it.getUser().equals(user))) {
      throw new RuntimeException("해당 채널에 참여중이지 않습니다."); // TODO: 예외처리
    }
    //4. 해당 채널 플레이리스트 가져오기.
    Playlist playlist = channel.getChannelPlaylist();
    //5. 비디오 추가.
    Video video = Video.createVideo(videoRequest, playlist.getVideoList());
    playlist.getVideoList().add(video);
    playlistRepository.save(playlist);
    channelRepository.save(channel);
    //6. 채널response로 반환.
    PlaylistResponse playlistResponse = PlaylistResponse.from(playlist);
    List<ParticipantResponse> participantResponseList = channel.getChannelParticipants().stream()
        .map(
            ParticipantResponse::createParticipantResponse).toList();
    return ChannelResponse.createChannelResponse(channel, playlistResponse,
        participantResponseList);
  }

  @Override
  @Transactional
  public ChannelResponse deleteVideoToChannel(Long channelId, Long id,
      CustomOAuth2User customOAuth2User) {
    //1. 로그인한 유저 정보 가져오기.
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    //2. 채널 ID를 통해 채널 가져오기.
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new RuntimeException("해당 채널이 없습니다.")); // TODO: 예외처리
    //3. 유저가 해당 채널의 Host인지 검증.
    if (channel.getChannelParticipants().stream().filter(it -> it.getIsHost().equals(true))
        .noneMatch(it -> it.getUser().equals(user))) {
      throw new RuntimeException("해당 채널의 호스트가 아닙니다."); // TODO: 예외처리
    }
    //4. 해당 채널 플레이리스트 가져오기.
    Playlist playlist = channel.getChannelPlaylist();
    //4-1. 플레이리스트에서 비디오 리스트 가져오기
    List<Video> videoList = playlist.getVideoList();
    //5. 플레이리스트의 video 중 id값이 같은 것 제거.
    Video video = videoList.stream().filter(it -> it.getId().equals(id)).findFirst()
        .orElseThrow(() -> new RuntimeException("해당하는 비디오가 없습니다."));
    videoList.remove(video);
    playlistRepository.save(playlist);
    channelRepository.save(channel);
    //6. 채널 response로 반환
    PlaylistResponse playlistResponse = PlaylistResponse.from(playlist);
    List<ParticipantResponse> participantResponseList = channel.getChannelParticipants().stream()
        .map(
            ParticipantResponse::createParticipantResponse).toList();
    return ChannelResponse.createChannelResponse(channel, playlistResponse,
        participantResponseList);
  }

  @Override
  @Transactional
  public UserPlaylistResponse savePlaylistToUserPlaylist(Long channelId, CustomOAuth2User customOAuth2User) {
    //1. 로그인 한 유저 정보를 가져온다.
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    //2. 채널 ID를 통해 채널을 가져온다.
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new RuntimeException("해당 채널이 없습니다."));// TODO: 예외처리
    //3. 채널의 플레이리스트 정보를 가져온다.
    Playlist playlist = channel.getChannelPlaylist();
    //4. 유저플레이리스트를 생성한다. 생성 시, 유저플레이리스트네임은 Playlist_uuid 값으로 저장한다.(추후 변경)
    UserPlaylist userPlaylist = UserPlaylist.fromChannelPlaylist(user, playlist);
    //5. 유저 플레이리스트를 저장한다.
    userPlaylistRepository.save(userPlaylist);
    //6. userPlaylistResponse로 반환
    return UserPlaylistResponse.from(userPlaylist);
  }
}
