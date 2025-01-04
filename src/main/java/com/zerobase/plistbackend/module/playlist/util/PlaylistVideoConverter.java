package com.zerobase.plistbackend.module.playlist.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.userplaylist.domain.Video;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class PlaylistVideoConverter implements AttributeConverter<Video, String> {

  private final ObjectMapper mapper;

  @Override
  public String convertToDatabaseColumn(Video video) {
    try {
      return mapper.writeValueAsString(video);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Could not convert Video to JSON", e);
    }
  }

  @Override
  public Video convertToEntityAttribute(String json) {
    try {
      return mapper.readValue(json, Video.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting JSON to Video", e);
    }
  }
}
