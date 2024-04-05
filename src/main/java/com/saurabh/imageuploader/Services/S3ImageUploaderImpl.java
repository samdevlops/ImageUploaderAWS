package com.saurabh.imageuploader.Services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.saurabh.imageuploader.Exceptions.ImageUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class S3ImageUploaderImpl implements S3ImageUploader{


    //DB & Other Dependencies

    @Autowired
    private AmazonS3 client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();

        String fileName = UUID.randomUUID().toString().concat(originalFileName.substring(originalFileName.lastIndexOf(".")));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());

        try {
            PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucketName, image.getOriginalFilename(), image.getInputStream(), metadata));
            return fileName;
        }
        catch (IOException e){
            throw new ImageUploadException("Images could not be uploaded due to some error..."+e.getMessage());
        }
    }

    @Override
    public List<String> allFiles() {
        return null;
    }

    @Override
    public String preSignedUrl() {
        return null;
    }
}
