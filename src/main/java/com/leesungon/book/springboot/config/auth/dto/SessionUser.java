package com.leesungon.book.springboot.config.auth.dto;

import com.leesungon.book.springboot.domain.posts.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPictrue();
    }
}
