package com.zerobase.plistbackend.module.channel.util;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ControllerApiResponse<T> {

  private List<T> content;
  private boolean hasNext;
}