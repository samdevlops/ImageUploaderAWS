package com.saurabh.imageuploader.Services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3ImageUploader {

    String uploadImage(MultipartFile image) throws IOException;

    List<String> allFiles();

    String preSignedUrl();
}
