package com.leesungon.book.springboot.web.dto;

import com.leesungon.book.springboot.domain.posts.like.Like;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import com.leesungon.book.springboot.domain.posts.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostsLikeRequestDto {

    private Integer likes;
    private Integer dislike;
    private LocalDateTime date = LocalDateTime.now();

    @Builder
    public PostsLikeRequestDto(Integer likes, Integer dislike, LocalDateTime date){
        this.likes = likes;
        this.dislike = dislike;
        this.date = date;

    }

    public Like toEntity(){
        return Like.builder()
                .likes(likes)
                .dislike(dislike)
                .likeDate(date)
                .build();
    }
}
