package com.zerobase.plistbackend.module.user.util;

import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.user.type.UserErrorStatus;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Util {

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  private final S3Client s3Client;

  public String putImage(MultipartFile multipartFile, String userEmail) {

    String fileName = userEmail + "." + getFileExtension(multipartFile.getOriginalFilename());

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .contentType(multipartFile.getContentType())
        .build();

    s3Upload(multipartFile, putObjectRequest);

    String fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
    log.info("S3 File Route : {}", fileUrl);
    return fileUrl;
  }

  private void s3Upload(MultipartFile multipartFile, PutObjectRequest putObjectRequest) {
    try {
      InputStream inputStream = multipartFile.getInputStream();
      PutObjectResponse response = s3Client.putObject(putObjectRequest,
          RequestBody.fromInputStream(inputStream,
              inputStream.available()));
      log.info("File uploaded successfully: {}", response);
    } catch (IOException e) {
      log.error("File upload IOException - ", e);
      throw new UserException(UserErrorStatus.UPLOAD_IMAGE_FAIL);
    }
  }

  private String getFileExtension(String fileName) {
    if (fileName == null || !fileName.contains(".")) {
      return "";
    }
    return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
  }
}
