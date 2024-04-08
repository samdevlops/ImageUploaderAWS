package com.saurabh.imageuploader.Services;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.saurabh.imageuploader.Exceptions.ImageUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), metadata));
            return this.preSignedUrl(fileName);
        }
        catch (IOException e){
            throw new ImageUploadException("Images could not be uploaded due to some error..."+e.getMessage());
        }
    }

    @Override
    public List<String> allFiles() {

        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName);

        ListObjectsV2Result listObjectsV2Result = client.listObjectsV2(listObjectsV2Request);

        List<S3ObjectSummary> s3ObjectSummaryList = listObjectsV2Result.getObjectSummaries();

        List<String> objectUrlList =s3ObjectSummaryList.stream().map(item -> this.preSignedUrl(item.getKey())).collect(Collectors.toList());

        return objectUrlList;
    }

    @Override
    public String preSignedUrl(String fileName) {
        // Getting Current Date
        Date expirationDate = new Date();

        //Getting current time
        long time = expirationDate.getTime();

        //Adding n hours to current time
        int hours = 1;
        time += hours * 60 * 60 * 1000;
        expirationDate.setTime(time);

        //Generating pre-signed URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expirationDate);


        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    @Override
    public String getUrlByFileName(String fileName) {
        S3Object object = client.getObject(bucketName, fileName);

        String key = object.getKey();

        return this.preSignedUrl(key);
    }
}
