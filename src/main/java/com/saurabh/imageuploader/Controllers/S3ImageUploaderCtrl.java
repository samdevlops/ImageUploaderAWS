package com.saurabh.imageuploader.Controllers;

import com.saurabh.imageuploader.Exceptions.ImageUploadException;
import com.saurabh.imageuploader.Services.S3ImageUploaderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/all")
    public List<String> allFiles(){
        return  s3ImageUploader.allFiles();
    }

    @GetMapping("/{fileName}")
    public String getUrlByFileName(@PathVariable("fileName") String fileName){
        return s3ImageUploader.getUrlByFileName(fileName);
    }
}
