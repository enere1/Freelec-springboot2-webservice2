package com.leesungon.book.springboot.web.dto;


import com.leesungon.book.springboot.domain.posts.Posts;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;
    private List<PostsUploadRequestDto> attachList = new ArrayList<PostsUploadRequestDto>();

    @Builder
    public PostsUpdateRequestDto(String title, String content , List<PostsUploadRequestDto>  attachList){
        this.title = title;
        this.content = content;
        this.attachList = attachList;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .postsUploadRequestDto(attachList)
                .build();
    }
}
