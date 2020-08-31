package com.leesungon.book.springboot.domain.posts;

import com.leesungon.book.springboot.domain.BaseTimeEntity;
import com.leesungon.book.springboot.domain.posts.like.Like;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import com.leesungon.book.springboot.domain.posts.user.User;
import com.leesungon.book.springboot.web.dto.PostsUploadRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "POSTS")
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition  = "TEXT", nullable = false)
    private String content;

    private String author;

    @OneToMany(mappedBy = "posts" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Upload> uploadList = new ArrayList<Upload>();

    @OneToMany(mappedBy = "posts" , cascade = CascadeType.ALL)
    private List<Like> likeList = new ArrayList<Like>();

    public void addUpload(Upload upload) {
        uploadList.add(upload);
        upload.setPosts(this);
    }

    public void addlike(Like like) {
        likeList.add(like);
        like.setPosts(this);
    }

    @Builder()
    public Posts(String title, String content, String author,List<PostsUploadRequestDto> postsUploadRequestDto){
        this.title = title;
        this.content = content;
        this.author = author;

        if(postsUploadRequestDto.size() > 0 ){
        for(PostsUploadRequestDto attachedFile : postsUploadRequestDto){
            System.out.println("line 59 : " + attachedFile.getFileName());
            Upload upload = attachedFile.toEntity();
            addUpload(upload);
          }
        }
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content, List<Upload> uploadList){
        this.title = title;
        this.content = content;
        this.uploadList = uploadList;
    }

}
