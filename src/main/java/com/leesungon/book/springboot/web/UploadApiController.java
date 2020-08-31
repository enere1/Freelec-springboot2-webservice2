
package com.leesungon.book.springboot.web;

import com.leesungon.book.springboot.service.posts.PostsService;
import com.leesungon.book.springboot.service.posts.UploadService;
import com.leesungon.book.springboot.web.dto.PostsUploadRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Log4j
public class UploadApiController {

    private final UploadService uploadService;
    private final PostsService postsService;

    @PostMapping(value = "/api/v1/postsUploadForm", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PostsUploadRequestDto> uploadForm(MultipartFile[] uploadFile) {

        List<PostsUploadRequestDto> list = new ArrayList<>();
        String uploadFolder = "C://upload";

        String uploadFolderPath = getFolder();
        //make folder
        File uploadPath = new File(uploadFolder, uploadFolderPath);
        log.info("upload path:" + uploadPath);

        //make yyyy/MM/dd folder
        if (uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {
            log.info("--------------");
            log.info("Upload file Name :" + multipartFile.getOriginalFilename());
            log.info("Upload file Size :" + multipartFile.getSize());


            String uploadFileName = multipartFile.getOriginalFilename();
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("//") + 1);
            //中腹防ぐ
            UUID uuid = UUID.randomUUID();

            String uuidFileName = uuid.toString() + "_" + uploadFileName;

            try {
                File saveFile = new File(uploadPath, uuidFileName);
                multipartFile.transferTo(saveFile);

                // check image type file
                if (CheckImageType(saveFile)) {
                    PostsUploadRequestDto postsUploadRequestDto = PostsUploadRequestDto.builder().fileName(uploadFileName)
                            .uploadPath(uploadFolderPath)
                            .uuid(uuid.toString())
                            .image(true)
                            .build();

                    File thumbnail = new File(uploadPath, "s_" + uuidFileName);

                    if (saveFile.exists()) {
                        Thumbnails.of(saveFile).size(190, 150).toFile(thumbnail);
                    //outputFormat("png").toFile(thumbnail);

                    }

                    list.add(postsUploadRequestDto);

                    for (int i = 0; i < list.size(); i++) {

                        log.info("getFileName:" + list.get(i).getFileName());
                        log.info("getUploadPath :" + list.get(i).getUploadPath());
                        log.info("getUuid:" + list.get(i).getUuid());
                    }
                } else {
                    PostsUploadRequestDto postsUploadRequestDto = PostsUploadRequestDto.builder().fileName(uploadFileName)
                            .uploadPath(uploadFolderPath)
                            .uuid(uuid.toString())
                            .image(false)
                            .build();

                    list.add(postsUploadRequestDto);
                }
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }

        //List<Long> uploadId = uploadService.save(list);
        //log.info("list size : " + list.size());
        //log.info("uploadId size : " + uploadId.size());

        /*for(int i =0; i < list.size(); i ++){
            log.info("uploadId : " + uploadId.get(i));
            list.get(i).setUploadId(uploadId.get(i));
        }*/

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
    public ResponseEntity deleteFile(String fileName , String type){
        log.info("delete  :" + fileName);

        File file;

        try {
            file = new File("c://upload//" + URLDecoder.decode(fileName,"UTF-8"));

            file.delete();

            if(type.equals("image")){
                String largeFileName = file.getAbsolutePath().replace("s_","");
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
    public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent , String fileName){

        log.info("donwload file:" + fileName);

        Resource resource = new FileSystemResource("C://upload//" + fileName);
        if(resource.exists() == false){
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }

        log.info("resource:" + resource);

        String resourceName = resource.getFilename();

        //remove UUID
        String resourceOriginalName = resourceName.substring(resourceName.lastIndexOf("_")+1);

        HttpHeaders headers =new HttpHeaders();

        try {
            String downloadName = null;
            if(userAgent.contains("Trident")){
                log.info("IE browser");
                downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8");
                log.info("IE name:" + downloadName );
            }else if(userAgent.contains("Edge")){
                log.info("Edge");
                downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8");
            } else{
                log.info("Chrome browser");
                downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
            }
            headers.add("Content-Disposition","attachment;fileName="+downloadName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
    }

    @GetMapping("/posts/display")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String fileName){

        log.info("file Name : " + fileName);

        File file = new File("c://upload//"+fileName);

        log.info("file :" + file);

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
    }
}

