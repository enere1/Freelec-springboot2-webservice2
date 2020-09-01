package com.leesungon.book.springboot.web.dto;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Log4j
@Service
@NoArgsConstructor
public class S3Service {

    private AmazonS3 s3Client;

    public static final String CLOUD_FRONT_DOMAIN_NAME = "dbri32o6g31a.cloudfront.net";

    private String accessKey = "AKIAXP6FS33J57HEE54T";

    private String secretKey = "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";

    private String bucket = "freelec-springboot-asemble-deploy";

    private String region = "ap-northeast-1";

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public List<PostsUploadRequestDto> putS3(List<PostsUploadRequestDto> list,MultipartFile[] uploadFile) throws IOException {

        log.info(" enter putS3");
        String fileName = null;
        log.info(list.size());
        for (int i = 0; i < list.size(); i++) {
            fileName = list.get(i).getFileName();
            log.info("put start : " + i );
            s3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile[i].getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            log.info("put end : " + i );
            list.get(i).setUploadPath("https://" + CLOUD_FRONT_DOMAIN_NAME + "/" + fileName);
            log.info(list.get(i).getUploadPath());
        }

        log.info(" putS3 process end ");

        return list;
    }

    public void deleteFile(){

    }


}
