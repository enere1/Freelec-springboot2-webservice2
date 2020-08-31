package com.leesungon.book.springboot.web.dto;

import com.leesungon.book.springboot.domain.posts.Posts;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import lombok.*;


@Data
@NoArgsConstructor
public class PostsUploadRequestDto {

    private String fileName;

    private String uploadPath = "";

    private String uuid;

    private boolean image;

    @Builder
    public PostsUploadRequestDto(String fileName, String uploadPath, String uuid, boolean image){
        this.fileName = fileName;
        this.uploadPath = uploadPath;
        this.uuid = uuid;
        this.image = image;
    }

    public Upload toEntity(){
        return Upload.builder()
                .fileName(fileName)
                .uploadPath(uploadPath)
                .uuid(uuid)
                .image(image)
                .build();
    }
}
