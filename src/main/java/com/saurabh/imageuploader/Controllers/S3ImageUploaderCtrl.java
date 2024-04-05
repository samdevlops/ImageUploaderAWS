package com.saurabh.imageuploader.Controllers;

import com.saurabh.imageuploader.Exceptions.ImageUploadException;
import com.saurabh.imageuploader.Services.S3ImageUploaderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class S3ImageUploaderCtrl
{
    @Autowired
    S3ImageUploaderImpl s3ImageUploader;
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file) throws IOException {
        //if(file == null) return new ResponseEntity<>(ImageUploadException("Images is required")).;

        return ResponseEntity.ok(s3ImageUploader.uploadImage(file));
    }
}
