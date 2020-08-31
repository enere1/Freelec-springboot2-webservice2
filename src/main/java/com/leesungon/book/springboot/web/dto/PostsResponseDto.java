package com.leesungon.book.springboot.web.dto;

import com.leesungon.book.springboot.domain.posts.Posts;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import com.leesungon.book.springboot.domain.posts.user.User;
import lombok.Getter;

import java.util.List;

@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private List<Upload> attachList;

    public PostsResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.attachList = entity.getUploadList();
    }
}
