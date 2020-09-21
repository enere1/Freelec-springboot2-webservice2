package com.leesungon.book.springboot.web;

import com.leesungon.book.springboot.config.auth.LoginUser;
import com.leesungon.book.springboot.config.auth.dto.SessionUser;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import com.leesungon.book.springboot.domain.posts.user.User;
import com.leesungon.book.springboot.service.posts.PostsService;
import com.leesungon.book.springboot.web.dto.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Log4j
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) throws Exception{
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id , @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findByID(@PathVariable Long id){
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }


    @GetMapping("/api/v1/posts/like/{id}")
    public Map selectPostLike(@LoginUser SessionUser user, @PathVariable Long id){

        Map likeMap = new HashMap<>();

        int totalLike = postsService.selectPostLike(id);
        int like = postsService.selectLikeUser(id, user);

        //結果をMapに入れる。
        likeMap.put("totalLike",totalLike);
        likeMap.put("like",like);

        return likeMap;
    }


    @PostMapping("/api/v1/posts/like/{id}")
    public Map postLike(@LoginUser SessionUser user, @PathVariable Long id , @RequestBody PostsLikeRequestDto postsLikeRequestDto){
        int totalLike = postsService.insertPostLike(id, user, postsLikeRequestDto);
        Map result = new HashMap<>();

        log.info("totalLike :" + totalLike);
        result.put("totalLikes", totalLike);

        int totalResult = postsService.selectPostLike(id);
        result.put("totalResult",totalResult);
        result.put("user",user.getName());

        return result;
    }

}
