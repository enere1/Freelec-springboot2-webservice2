package com.leesungon.book.springboot.domain.posts.upload;

import com.leesungon.book.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class Upload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String uploadPath;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private boolean image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POSTS_ID")
    private Posts posts;

    @Builder
    public Upload(String fileName, String uploadPath, String uuid, boolean image){
        this.fileName = fileName;
        this.uploadPath = uploadPath;
        this.uuid = uuid;
        this.image = image;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }
}
