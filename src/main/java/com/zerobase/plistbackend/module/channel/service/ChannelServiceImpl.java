package com.zerobase.plistbackend.module.channel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.category.entity.Category;
import com.zerobase.plistbackend.module.category.exception.CategoryException;
import com.zerobase.plistbackend.module.category.repository.CategoryRepository;
import com.zerobase.plistbackend.module.category.type.CategoryErrorStatus;
import com.zerobase.plistbackend.module.channel.domain.HostExitEvent;
import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.DetailChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.DetailClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.exception.ChannelException;
import com.zerobase.plistbackend.module.channel.repository.ChannelRepository;
import com.zerobase.plistbackend.module.channel.repository.CustomChannelRepository;
import com.zerobase.plistbackend.module.channel.type.ChannelErrorStatus;
import com.zerobase.plistbackend.module.channel.type.ChannelStatus;
import com.zerobase.plistbackend.module.home.exception.VideoException;
import com.zerobase.plistbackend.module.home.model.Video;
import com.zerobase.plistbackend.module.home.type.VideoErrorStatus;
import com.zerobase.plistbackend.module.participant.entity.Participant;
import com.zerobase.plistbackend.module.participant.repository.ParticipantRepository;
import com.zerobase.plistbackend.module.playlist.domain.PlaylistCrudEvent;
import com.zerobase.plistbackend.module.playlist.util.PlaylistVideoConverter;
import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.user.model.auth.CustomOAuth2User;
import com.zerobase.plistbackend.module.user.repository.UserRepository;
import com.zerobase.plistbackend.module.userplaylist.dto.request.VideoRequest;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import com.zerobase.plistbackend.module.userplaylist.exception.UserPlaylistException;
import com.zerobase.plistbackend.module.userplaylist.repository.UserPlaylistRepository;
import com.zerobase.plistbackend.module.userplaylist.type.UserPlaylistErrorStatus;
import com.zerobase.plistbackend.module.userplaylist.util.UserPlaylistUtil;
import java.util.List;

import com.zerobase.plistbackend.module.websocket.domain.VideoSyncManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final CategoryRepository categoryRepository;
  private final ParticipantRepository participantRepository;
  private final UserPlaylistRepository userPlaylistRepository;
  private final CustomChannelRepository customChannelRepository;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final VideoSyncManager videoSyncManager;

  @Override
  @Transactional
  public DetailChannelResponse addChannel(CustomOAuth2User customOAuth2User,
      ChannelRequest channelRequest) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    Category category = categoryRepository.findById(channelRequest.getCategoryId())
        .orElseThrow(() -> new CategoryException(CategoryErrorStatus.NOT_FOUND));

    if (participantRepository.existsByUser(user)) {
      throw new ChannelException(ChannelErrorStatus.ALREADY_ENTER);
    }

    Channel channel = Channel.createChannel(channelRequest, user, category);

    if (channelRequest.getUserPlaylistId() != null) {
      UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistId(user,
          channelRequest.getUserPlaylistId()).orElseThrow(() -> new UserPlaylistException(
          UserPlaylistErrorStatus.NOT_FOUND));
      channel.getChannelPlaylist().setVideoList(userPlaylist.getVideoList());
    }

    channelRepository.save(channel);

    return DetailChannelResponse.createDetailChannelResponse(channel, user);
  }

  @Override
  @Transactional
  public DetailChannelResponse userEnterChannel(CustomOAuth2User customOAuth2User, Long channelId) {
    Channel channel = channelRepository.findByChannelIdAndChannelStatus(channelId,
            ChannelStatus.CHANNEL_STATUS_ACTIVE)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    if (user.getParticipant() != null) {
      throw new ChannelException(ChannelErrorStatus.ALREADY_ENTER);
    }

    Participant participant = Participant.viewer(user, channel);
    channel.getChannelParticipants().add(participant);
    channelRepository.save(channel);

    return DetailChannelResponse.createDetailChannelResponse(channel, user);
  } // TODO: 테스트코드를 활용해 테스트 필요.

  @Override
  @Transactional
  public void userExitChannel(CustomOAuth2User customOAuth2User, Long channelId) {
    Channel channel = channelRepository.findByChannelIdAndChannelStatus(channelId,
            ChannelStatus.CHANNEL_STATUS_ACTIVE)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    if (user.getParticipant() == null || !user.getParticipant().getChannel().equals(channel)) {
      throw new ChannelException(ChannelErrorStatus.NOT_ENTER);
    }
    Participant participant = participantRepository.findByUser(user);
    channel.removeParticipant(participant);
    channelRepository.save(channel);
  } // TODO: 테스트코드를 활용해 테스트 필요.

  @Override
  @Transactional
  public void hostExitChannel(CustomOAuth2User customOAuth2User, Long channelId) {
    Channel channel = channelRepository.findByChannelIdAndChannelStatus(channelId,
            ChannelStatus.CHANNEL_STATUS_ACTIVE)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    if (!channel.getChannelHost().equals(user)) {
      throw new ChannelException(ChannelErrorStatus.NOT_HOST);
    }

    applicationEventPublisher.publishEvent(new HostExitEvent(channelId));
    Channel.closeChannel(channel);
    channelRepository.save(channel);
    videoSyncManager.removeCurrentTime(channelId);
  }

  @Override
  @Transactional
  public void addVideoToChannel(Long channelId, VideoRequest videoRequest,
      CustomOAuth2User customOAuth2User) {
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    Channel channelWithLock = channelRepository.findByIdWithLock(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    if (!user.getParticipant().getChannel().equals(channelWithLock)) {
      throw new ChannelException(ChannelErrorStatus.NOT_ENTER);
    }

    channelWithLock.getChannelPlaylist().getVideoList()
        .add(Video.createVideo(videoRequest, channelWithLock.getChannelPlaylist().getVideoList()));

    channelRepository.save(channelWithLock);
    applicationEventPublisher.publishEvent(new PlaylistCrudEvent(channelId, customOAuth2User));

  }

  @Override
  @Transactional
  public void deleteVideoToChannel(Long channelId, Long id,
      CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    Channel channelWithLock = channelRepository.findByIdWithLock(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    if (!channelWithLock.getChannelHost().equals(user)) {
      throw new ChannelException(ChannelErrorStatus.NOT_HOST);
    }

    if (channelWithLock.getChannelStatus().equals(ChannelStatus.CHANNEL_STATUS_CLOSED)) {
      throw new ChannelException(ChannelErrorStatus.NOT_STREAMING);
    }

    Video video = channelWithLock.getChannelPlaylist().getVideoList().stream()
        .filter(it -> it.getId().equals(id)).findFirst()
        .orElseThrow(() -> new VideoException(VideoErrorStatus.NOT_EXIST));
    channelWithLock.getChannelPlaylist().getVideoList().remove(video);

    channelRepository.save(channelWithLock);
    applicationEventPublisher.publishEvent(new PlaylistCrudEvent(channelId, customOAuth2User));
  }

  @Override
  @Transactional
  public void updateChannelPlaylist(Long channelId,
      String updateChannelPlaylistJson, CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    Channel channelWithLock = channelRepository.findByIdWithLock(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    if (!channelWithLock.getChannelHost().equals(user)) {
      throw new ChannelException(ChannelErrorStatus.NOT_HOST);
    }

    PlaylistVideoConverter playlistVideoConverter = new PlaylistVideoConverter(new ObjectMapper());
    List<Video> videoList = playlistVideoConverter.convertToEntityAttribute(
        updateChannelPlaylistJson);

    channelWithLock.getChannelPlaylist().setVideoList(videoList);

    channelRepository.save(channelWithLock);
    applicationEventPublisher.publishEvent(new PlaylistCrudEvent(channelId, customOAuth2User));
  }

  @Override
  @Transactional
  public void savePlaylistToUserPlaylist(Long channelId,
      CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    UserPlaylist userPlaylist = UserPlaylist.fromChannelPlaylist(
        user, channel);

    if (userPlaylistRepository.existsByUserAndUserPlaylistName(user,
        userPlaylist.getUserPlaylistName())) {
      userPlaylist.setUserPlaylistName(
          new UserPlaylistUtil(userPlaylistRepository).generateNextName(user,
              userPlaylist.getUserPlaylistName()));
    }

    userPlaylistRepository.save(userPlaylist);
  }

  @Override
  @Transactional
  public void likeVideo(CustomOAuth2User customOAuth2User, VideoRequest videoRequest) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    String favorite = "favorite";
    UserPlaylist userPlaylist = userPlaylistRepository.findByUserAndUserPlaylistName(user, favorite)
        .orElseThrow(() -> new UserPlaylistException(UserPlaylistErrorStatus.NOT_FOUND));

    userPlaylist.getVideoList().add(Video.createVideo(videoRequest, userPlaylist.getVideoList()));

    userPlaylistRepository.save(userPlaylist);
  }

  //검색 관련 서비스

  @Override
  @Transactional(readOnly = true)
  public Slice<StreamingChannelResponse> findChannelList(Long cursorId, Pageable pageable) {

    return customChannelRepository.findStreamingChannelOrderByChannelId(cursorId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Slice<StreamingChannelResponse> findChannelListPopular(Long cursorId, Long cursorPopular,
      Pageable pageable) {

    return customChannelRepository.findStreamingChannelOrderByParticipantCount(cursorId,
        cursorPopular, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public List<StreamingChannelResponse> searchChannel(String searchValue) {
    List<Channel> channelList = channelRepository.search(
        ChannelStatus.CHANNEL_STATUS_ACTIVE, searchValue, searchValue, searchValue);

    return channelList.stream().map(StreamingChannelResponse::createStreamingChannelResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Slice<StreamingChannelResponse> findChannelFromChannelCategory(Long categoryId,
      Long cursorId, Long cursorPopular, Pageable pageable) {

    return customChannelRepository.findStreamingChannelFromCategoryIdOrderByParticipantCount(
        categoryId, cursorId, cursorPopular, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public DetailChannelResponse findOneChannel(Long channelId, CustomOAuth2User customOAuth2User) {
    Channel channel = channelRepository.findByChannelIdAndChannelStatus(channelId,
            ChannelStatus.CHANNEL_STATUS_ACTIVE)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    if (!participantRepository.existsByUser(user)) {
      throw new ChannelException(ChannelErrorStatus.NOT_ENTER);
    }

    return DetailChannelResponse.createDetailChannelResponse(channel, user);
  }

  @Override
  @Transactional(readOnly = true)
  public Slice<ClosedChannelResponse> findUserChannelHistory(CustomOAuth2User customOAuth2User,
      Long cursorId, Pageable pageable) {

    User requestUser = userRepository.findByUserEmail(customOAuth2User.findEmail());

    return customChannelRepository.findClosedChannelOrderByChannelId(requestUser, cursorId,
        pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public DetailClosedChannelResponse findOneUserChannelHistory(CustomOAuth2User customOAuth2User,
      Long channelId) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    Channel channel = channelRepository.findByChannelIdAndChannelHost(channelId, user)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    return DetailClosedChannelResponse.createClosedChannelResponse(channel, user);
  }
}

