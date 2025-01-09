package com.zerobase.plistbackend.module.home.service;

import com.zerobase.plistbackend.module.home.model.Video;
import java.io.IOException;
import java.util.List;
import org.json.simple.parser.ParseException;

public interface HomeService {

  List<Video> searchVideo(String keyword) throws IOException, ParseException;

}
