package com.zerobase.plistbackend.module.channel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.category.entity.Category;
import com.zerobase.plistbackend.module.category.exception.CategoryException;
import com.zerobase.plistbackend.module.category.repository.CategoryRepository;
import com.zerobase.plistbackend.module.category.type.CategoryErrorStatus;
import com.zerobase.plistbackend.module.channel.dto.request.ChannelRequest;
import com.zerobase.plistbackend.module.channel.dto.response.ClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.DetailChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.DetailClosedChannelResponse;
import com.zerobase.plistbackend.module.channel.dto.response.StreamingChannelResponse;
import com.zerobase.plistbackend.module.channel.entity.Channel;
import com.zerobase.plistbackend.module.channel.exception.ChannelException;
import com.zerobase.plistbackend.module.channel.repository.ChannelRepository;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final CategoryRepository categoryRepository;
  private final ParticipantRepository participantRepository;
  private final UserPlaylistRepository userPlaylistRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  @Transactional
  public void addChannel(CustomOAuth2User customOAuth2User,
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
  }

  @Override
  @Transactional(readOnly = true)
  public List<StreamingChannelResponse> findChannelList() {
    List<Channel> channelList = channelRepository.findAllByChannelStatusSortedChannelIdDesc(
        ChannelStatus.CHANNEL_STATUS_ACTIVE);

    return channelList.stream().map(StreamingChannelResponse::createStreamingChannelResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<StreamingChannelResponse> findChannelListPopular() {
    List<Channel> channelList = channelRepository.findAllByChannelStatusSortedByParticipantCountDesc(
        ChannelStatus.CHANNEL_STATUS_ACTIVE);

    return channelList.stream().map(StreamingChannelResponse::createStreamingChannelResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public DetailChannelResponse findOneChannel(Long channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));
    return DetailChannelResponse.createDetailChannelResponse(channel);
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
  public List<StreamingChannelResponse> findChannelFromChannelCategory(Long categoryId) {
    List<Channel> channelList = channelRepository.findByChannelStatusAndCategorySortedParticipantCountDesc(
        ChannelStatus.CHANNEL_STATUS_ACTIVE,
        categoryId);

    return channelList.stream().map(StreamingChannelResponse::createStreamingChannelResponse)
        .toList();
  }


  @Override
  @Transactional
  public void enterChannel(CustomOAuth2User customOAuth2User, Long channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    if (user.getParticipant() != null) {
      throw new ChannelException(ChannelErrorStatus.ALREADY_ENTER);
    }

    Participant participant = Participant.viewer(user, channel);
    channel.getChannelParticipants().add(participant);
    channelRepository.save(channel);
  } // TODO: 테스트코드를 활용해 테스트 필요.

  @Override
  @Transactional
  public void userExitChannel(CustomOAuth2User customOAuth2User, Long channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    Participant participant = participantRepository.findByUser(user);

    if (!user.getParticipant().getChannel().equals(channel)) {
      throw new ChannelException(ChannelErrorStatus.NOT_ENTER);
    }
    channel.removeParticipant(participant);
    channelRepository.save(channel);
  } // TODO: 테스트코드를 활용해 테스트 필요.

  @Override
  @Transactional
  public void hostExitChannel(CustomOAuth2User customOAuth2User, Long channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    if (!channel.getChannelHost().equals(user.getUserName())) {
      throw new ChannelException(ChannelErrorStatus.NOT_HOST);
    }
    Channel.closeChannel(channel);
    channelRepository.save(channel);
  }

  @Override
  @Transactional
  public void addVideoToChannel(Long channelId, VideoRequest videoRequest,
      CustomOAuth2User customOAuth2User) {
    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());
    System.out.println("user = " + user);

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    if (!user.getParticipant().getChannel().equals(channel)) {
      throw new ChannelException(ChannelErrorStatus.NOT_ENTER);
    }

    channel.getChannelPlaylist().getVideoList()
        .add(Video.createVideo(videoRequest, channel.getChannelPlaylist().getVideoList()));

    channelRepository.save(channel);
    applicationEventPublisher.publishEvent(new PlaylistCrudEvent(channelId));

  }

  @Override
  @Transactional
  public void deleteVideoToChannel(Long channelId, Long id,
      CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    if (!channel.getChannelHost().equals(user.getUserName())) {
      throw new ChannelException(ChannelErrorStatus.NOT_HOST);
    }

    if (channel.getChannelStatus().equals(ChannelStatus.CHANNEL_STATUS_CLOSED)) {
      throw new ChannelException(ChannelErrorStatus.NOT_STREAMING);
    }

    Video video = channel.getChannelPlaylist().getVideoList().stream()
        .filter(it -> it.getId().equals(id)).findFirst()
        .orElseThrow(() -> new VideoException(VideoErrorStatus.NOT_EXIST));
    channel.getChannelPlaylist().getVideoList().remove(video);

    channelRepository.save(channel);
    applicationEventPublisher.publishEvent(new PlaylistCrudEvent(channelId));
  }

  @Override
  @Transactional
  public void savePlaylistToUserPlaylist(Long channelId,
      CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    if (channel.getChannelStatus().equals(ChannelStatus.CHANNEL_STATUS_CLOSED)) {
      throw new ChannelException(ChannelErrorStatus.NOT_STREAMING);
    }

    UserPlaylist userPlaylist = UserPlaylist.fromChannelPlaylist(
        user, channel);

    userPlaylistRepository.save(userPlaylist);
  }

  @Override
  @Transactional
  public void updateChannelPlaylist(Long channelId,
      String updateChannelPlaylistJson) {

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    PlaylistVideoConverter playlistVideoConverter = new PlaylistVideoConverter(new ObjectMapper());
    List<Video> videoList = playlistVideoConverter.convertToEntityAttribute(
        updateChannelPlaylistJson);

    channel.getChannelPlaylist().setVideoList(videoList);

    channelRepository.save(channel);
    applicationEventPublisher.publishEvent(new PlaylistCrudEvent(channelId));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ClosedChannelResponse> findUserChannelHistory(CustomOAuth2User customOAuth2User) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    List<Channel> channelList = channelRepository.findByChannelHostAndChannelStatus(
        user.getUserName(), ChannelStatus.CHANNEL_STATUS_CLOSED);

    return channelList.stream().map(ClosedChannelResponse::createClosedChannelResponse).toList();
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

  @Override
  @Transactional(readOnly = true)
  public DetailClosedChannelResponse findOneUserChannelHistory(CustomOAuth2User customOAuth2User,
      Long channelId) {

    User user = userRepository.findByUserEmail(customOAuth2User.findEmail());

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelException(ChannelErrorStatus.NOT_FOUND));

    return DetailClosedChannelResponse.createClosedChannelResponse(channel);
  }
}
