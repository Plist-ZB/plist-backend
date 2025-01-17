package com.zerobase.plistbackend.module.userplaylist.util;

import com.zerobase.plistbackend.module.user.entity.User;
import com.zerobase.plistbackend.module.userplaylist.entity.UserPlaylist;
import com.zerobase.plistbackend.module.userplaylist.repository.UserPlaylistRepository;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;

@RequiredArgsConstructor
public class UserPlaylistUtil {

  private final UserPlaylistRepository userPlaylistRepository;

  public String generateNextName(User user, String userPlaylistName) {
    List<UserPlaylist> userPlaylists = userPlaylistRepository.findByUserAndUserPlaylistNameContaining(
        user, userPlaylistName);

    String newString = userPlaylistName;
    int maxNum = 0;

    Pattern pattern = Pattern.compile(Pattern.quote(userPlaylistName) + "\\((\\d+)\\)");
    for (UserPlaylist userPlaylist : userPlaylists) {
      Matcher matcher = pattern.matcher(userPlaylist.getUserPlaylistName());
      if (matcher.matches()) {
        int num = Integer.parseInt(matcher.group(1));
        if (num > maxNum) {
          maxNum = num;
        }
      }
    }

    newString = userPlaylistName + "(" + (maxNum + 1) + ")";

    return newString;
  }
}
