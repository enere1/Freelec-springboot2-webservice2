package com.leesungon.book.springboot.domain.posts.like;

import com.leesungon.book.springboot.domain.posts.Posts;
import com.leesungon.book.springboot.domain.posts.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "LIKES")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int likes;

    @Column
    private int dislike;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POSTS_ID")
    private Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column
    private LocalDateTime likeDate;

    @Builder
    public Like(int likes, int dislike, LocalDateTime likeDate){
        this.likes = likes;
        this.dislike = dislike;
        this.likeDate = likeDate;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
