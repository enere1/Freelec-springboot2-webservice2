package com.leesungon.book.springboot.domain.posts;

import com.leesungon.book.springboot.web.dto.PostsUploadRequestDto;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup()
    {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기(){
        //given
        String title ="테스트 게시글";
        String content = "테스트 본문";

        PostsUploadRequestDto postsUploadRequestDto = PostsUploadRequestDto.builder()
                .fileName("tes1")
                .uploadPath("C://upload//2020//07//01")
                .uuid("abcde")
                .image(true)
                .build();

        List<PostsUploadRequestDto> postsUploadRequestDtoList = new ArrayList<PostsUploadRequestDto>();
        postsUploadRequestDtoList.add(postsUploadRequestDto);

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("cbq3737@gmail.com")
                .postsUploadRequestDto(postsUploadRequestDtoList)
                .build());


        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_등록(){

        PostsUploadRequestDto postsUploadRequestDto = PostsUploadRequestDto.builder()
                .fileName("tes1")
                .uploadPath("C://upload//2020//07//01")
                .uuid("abcde")
                .image(true)
                .build();

        List<PostsUploadRequestDto> postsUploadRequestDtoList = new ArrayList<PostsUploadRequestDto>();
        postsUploadRequestDtoList.add(postsUploadRequestDto);

        //given
        LocalDateTime now = LocalDateTime.of(2019,6,4,0,0,0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .postsUploadRequestDto(postsUploadRequestDtoList)
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        System.out.println(">>>>> createDate="+posts.getCreatedDate()+", modifiedDate="+posts.getModifiedDate());

        assertThat(posts.getCreatedDate().isAfter(now));
        assertThat(posts.getModifiedDate().isAfter(now));

    }
}

