package com.zerobase.plistbackend.module.playlist.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.home.model.Video;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class PlaylistVideoConverter implements AttributeConverter<List<Video>, String> {

  private final ObjectMapper mapper;

  @Override
  public String convertToDatabaseColumn(List<Video> video) {
    try {
      return mapper.writeValueAsString(video);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Could not convert Video to JSON", e);
    }
  }

  @Override
  public List<Video> convertToEntityAttribute(String json) {
    try {
      return mapper.readValue(json,
          mapper.getTypeFactory().constructCollectionType(List.class, Video.class));
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting JSON to Video", e);
    }
  }
}
