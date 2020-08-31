package com.leesungon.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leesungon.book.springboot.domain.posts.Posts;
import com.leesungon.book.springboot.domain.posts.PostsRepository;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import com.leesungon.book.springboot.domain.posts.upload.UploadRepository;
import com.leesungon.book.springboot.web.dto.PostsResponseDto;
import com.leesungon.book.springboot.web.dto.PostsUpdateRequestDto;
import com.leesungon.book.springboot.web.dto.PostsUploadRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UploadsApiControllerTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    UploadRepository uploadRepository;

    @Autowired
    EntityManager em;

    @Test
    @WithMockUser(roles = "USER")
    public void register_upload() {

            //given
            Upload upload = uploadRepository.save(Upload.builder()
                    .fileName("tes1")
                    .uploadPath("C://upload//2020//07//01")
                    .uuid("abcde")
                    .image(true)
                    .build());

        PostsUploadRequestDto postsUploadRequestDto = PostsUploadRequestDto.builder()
                .fileName("tes1")
                .uploadPath("C://upload//2020//07//01")
                .uuid("abcde")
                .image(true)
                .build();

        List<PostsUploadRequestDto> postsUploadRequestDtoList = new ArrayList<PostsUploadRequestDto>();
        postsUploadRequestDtoList.add(postsUploadRequestDto);

            Posts posts = postsRepository.save(Posts.builder()
                    .title("title")
                    .content("content")
                    .author("author")
                    .postsUploadRequestDto(postsUploadRequestDtoList)
                    .build());
            posts.addUpload(upload);
            em.persist(posts);


    }

    @Test
    @WithMockUser(roles = "USER")
    public void cascade() {

        //given
        PostsUploadRequestDto postsUploadRequestDto = PostsUploadRequestDto.builder()
                .fileName("tes1")
                .uploadPath("C://upload//2020//07//01")
                .uuid("abcde")
                .image(true)
                .build();

        List<PostsUploadRequestDto> postsUploadRequestDtoList = new ArrayList<PostsUploadRequestDto>();
        postsUploadRequestDtoList.add(postsUploadRequestDto);
        
        Posts post = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .postsUploadRequestDto(postsUploadRequestDtoList)
                .build();

        System.out.println(post.getUploadList().size());

        em.persist(post);

        em.flush();
        em.clear();

        //when
        Posts findPost = em.find(Posts.class, post.getId());
        Upload findUpload = findPost.getUploadList().get(0);

        //cascade 때문에 객체 연관관계 남아있으면 삭제 안됨,
        //객체 연관관계가 남아있으면, cascade 때문에 post -> upload 관계가 남아있다고 생각해서 삭제가 안됨

        //em.remove을 생략하려면 post -> upload 관계에 orphanRemoval=true 추가 필요
        //em.remove(findUpload);

        em.flush();
        em.clear();

        Posts findPost2 = em.find(Posts.class, post.getId());
        System.out.println(findPost2.getUploadList().size());
        //then: upload는 제거되어야 한다.
        //Upload removedUpload = em.find(Upload.class, upload.getId());
        //Assertions.assertThat(removedUpload).isNull();
    }

}
