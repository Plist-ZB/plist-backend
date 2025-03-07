package com.zerobase.plistbackend.module.user.dto.request;

import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.user.type.UserErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public record UserProfileRequest(MultipartFile image, String nickname) {

  private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB
  private static final String[] ALLOWED_EXTENSIONS = {"jpg", "png", "webp"};

  public void validate() {
    if (image != null) {
      validateFileSize();
      validateFileExtension();
    }
  }

  private void validateFileSize() {
    if (image.getSize() > MAX_FILE_SIZE) {
      log.error("File size exceeds the maximum limit of 20MB.");
      throw new UserException(UserErrorStatus.FILE_SIZE_EXCEEDED);
    }
  }

  private void validateFileExtension() {
    String fileName = image.getOriginalFilename();
    if (!isExtensionValid(fileName)) {
      log.error("Invalid file type. Only jpg, png, and webp files are allowed.");
      throw new UserException(UserErrorStatus.INVALID_FILE_TYPE);
    }
  }

  private boolean isExtensionValid(String fileName) {
    String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    for (String allowedExtension : ALLOWED_EXTENSIONS) {
      if (extension.equals(allowedExtension)) {
        return true;
      }
    }
    return false;
  }

}