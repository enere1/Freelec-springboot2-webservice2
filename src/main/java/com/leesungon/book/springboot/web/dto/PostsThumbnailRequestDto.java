package com.leesungon.book.springboot.web.dto;

import com.leesungon.book.springboot.domain.posts.upload.Upload;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostsThumbnailRequestDto {

    private String fileName;

    private String uploadPath = "";

    private String uuid;

    private boolean image;

    @Builder
    public PostsThumbnailRequestDto(String fileName, String uploadPath, String uuid, boolean image){
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
