
package com.leesungon.book.springboot.web;

import com.leesungon.book.springboot.service.posts.PostsService;
import com.leesungon.book.springboot.service.posts.UploadService;
import com.leesungon.book.springboot.web.dto.PostsThumbnailRequestDto;
import com.leesungon.book.springboot.web.dto.PostsUploadRequestDto;
import com.leesungon.book.springboot.web.dto.S3Service;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.leesungon.book.springboot.web.dto.S3Service.CLOUD_FRONT_DOMAIN_NAME;

@RestController
@Log4j
@RequiredArgsConstructor
public class UploadApiController {

    private final S3Service s3Service;

    @PostMapping(value = "/api/v1/postsUploadForm", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PostsUploadRequestDto> uploadForm(MultipartFile[] uploadFile) throws IOException {

        List<PostsUploadRequestDto> list = new ArrayList<>();

        for (MultipartFile multipartFile : uploadFile) {
            log.info("--------------");
            log.info("Upload file Name :" + multipartFile.getOriginalFilename());
            log.info("Upload file Size :" + multipartFile.getSize());
            try {
                log.info("Upload file inputStream :" + multipartFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String uploadFileName = multipartFile.getOriginalFilename();
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("//") + 1);
            //中腹防ぐ
            UUID uuid = UUID.randomUUID();

            try {
                // check image type file
                PostsUploadRequestDto postsUploadRequestDto = PostsUploadRequestDto.builder().fileName(uploadFileName)
                        .uploadPath("null")
                        .uuid(uuid.toString())
                        .image(true)
                        .build();

                list.add(postsUploadRequestDto);

                for (int i = 0; i < list.size(); i++) {

                    log.info("getFileName:" + list.get(i).getFileName());
                    log.info("getUploadPath :" + list.get(i).getUploadPath());
                    log.info("getUuid:" + list.get(i).getUuid());
                }

            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }

        List<PostsUploadRequestDto> postsUploadRequestDtoList = s3Service.putS3(list, uploadFile);
        for (PostsUploadRequestDto postsUploadRequestDto : postsUploadRequestDtoList) {
            log.info(postsUploadRequestDto.getFileName());
            log.info(postsUploadRequestDto.getUuid());
            log.info(postsUploadRequestDto.getUploadPath());
            log.info(postsUploadRequestDto.isImage());
        }

        return list;
    }

    private String getFolder() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        String str = sdf.format(date);

        return str.replace("-", File.separator);
    }

    private boolean CheckImageType(File file) {
        try {
            String contentType = Files.probeContentType(file.toPath());

            return contentType.startsWith("image");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @DeleteMapping("/api/v1/posts/deleteFile")
    @ResponseBody
    public ResponseEntity deleteFile(String fileName, String type) {
        log.info("delete  :" + fileName);

        File file;

        try {
            file = new File("c://upload//" + URLDecoder.decode(fileName, "UTF-8"));

            file.delete();

            if (type.equals("image")) {
                String largeFileName = file.getAbsolutePath().replace("s_", "");
                log.info("largeFileName : " + largeFileName);
                file = new File(largeFileName);
                file.delete();
            }

            //uploadService.deleteUpload(id);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>("deleted", HttpStatus.OK);
    }

    @GetMapping(value = "/posts/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) throws MalformedURLException {

        log.info("donwload file:" + fileName);
        //fileName = fileName.substring(36,fileName.length());

        UrlResource resource = new UrlResource(fileName);
        if (resource.exists() == false) {
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }

        log.info("resource:" + resource);

        String resourceName = resource.getFilename();

        HttpHeaders headers = new HttpHeaders();

        try {
            String downloadName = null;
            if (userAgent.contains("Trident")) {
                log.info("IE browser");
                downloadName = URLEncoder.encode(resourceName, "UTF-8");
                log.info("IE name:" + downloadName);
            } else if (userAgent.contains("Edge")) {
                log.info("Edge");
                downloadName = URLEncoder.encode(resourceName, "UTF-8");
            } else {
                log.info("Chrome browser");
                downloadName = new String(resourceName.getBytes("UTF-8"), "ISO-8859-1");
            }
            headers.add("Content-Disposition", "attachment;fileName=" + downloadName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

    /*@GetMapping("/posts/display")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String fileName){

        log.info("file Name : " + fileName);

        File file = new File(fileName);
        file.toString().replace("https:\\","https:///");
        log.info("file :" + file);
        log.info("file.toPath() :" + file.toPath());

        ResponseEntity<byte[]> result = null;

        HttpHeaders header = new HttpHeaders();
        try {
            header.add("Content-type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header, HttpStatus.OK);
            log.info("result : " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }*/
}

