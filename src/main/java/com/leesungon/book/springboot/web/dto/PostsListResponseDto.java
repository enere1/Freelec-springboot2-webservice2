package com.leesungon.book.springboot.web.dto;

import com.leesungon.book.springboot.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String Author;
    private LocalDateTime modifiedDate;
    private int totalLikes;

    public PostsListResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.Author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();

    }

    public void setTotalLikes(int totalLikes){
        this.totalLikes = totalLikes;
    }
}
