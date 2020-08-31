package com.leesungon.book.springboot.web.dto;

import com.leesungon.book.springboot.domain.posts.Posts;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String title;
    private String content;
    private String author;
    private List<PostsUploadRequestDto> attachList;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author , List<PostsUploadRequestDto>  attachList){

        this.title = title;
        this.content = content;
        this.author = author;
        this.attachList = attachList;

    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .postsUploadRequestDto(attachList)
                .build();
    }




}
