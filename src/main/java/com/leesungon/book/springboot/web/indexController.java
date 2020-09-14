package com.leesungon.book.springboot.web;

import com.leesungon.book.springboot.config.auth.LoginUser;
import com.leesungon.book.springboot.config.auth.dto.SessionUser;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import com.leesungon.book.springboot.service.posts.PostsService;
import com.leesungon.book.springboot.web.dto.PostsListResponseDto;
import com.leesungon.book.springboot.web.dto.PostsResponseDto;


import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Log4j
public class indexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){
        List<PostsListResponseDto> postsList = postsService.findAllDesc();
        for(PostsListResponseDto postsListResponseDto : postsList){
            int totalLikes = postsService.selectPostLike(postsListResponseDto.getId());
            postsListResponseDto.setTotalLikes(totalLikes);
        }

        model.addAttribute("posts", postsList);


        if(user != null){
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(@LoginUser SessionUser user, Model model){
        model.addAttribute("userName", user.getName());
        return "posts-save";
    }


    @GetMapping("/posts/detail/{id}")
    public String postsDetail(@LoginUser SessionUser user,@PathVariable Long id, Model model){
        log.info("detail : " + id);

        PostsResponseDto dto = postsService.findById(id);

        List<Upload> attachList = dto.getAttachList();

        model.addAttribute("userName",user.getName());
        model.addAttribute("post",dto);
        return "posts-detail";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post",dto);
        return "posts-update";
    }

    @GetMapping("/posts/uploadAjax")
    public String postsUploadForm(){
        return "posts-uploadAjax";
    }


}