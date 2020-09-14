package com.leesungon.book.springboot.service.posts;

import com.leesungon.book.springboot.config.auth.dto.SessionUser;
import com.leesungon.book.springboot.domain.posts.Posts;
import com.leesungon.book.springboot.domain.posts.PostsRepository;
import com.leesungon.book.springboot.domain.posts.like.Like;
import com.leesungon.book.springboot.domain.posts.like.LikeRepository;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import com.leesungon.book.springboot.domain.posts.upload.UploadRepository;
import com.leesungon.book.springboot.domain.posts.user.Role;
import com.leesungon.book.springboot.domain.posts.user.User;
import com.leesungon.book.springboot.domain.posts.user.UserRepository;
import com.leesungon.book.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j
public class PostsService {

    private final PostsRepository postsRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final EntityManager em;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        List<PostsUploadRequestDto> attachList = requestDto.getAttachList();
        for (PostsUploadRequestDto uploadRequestDto : attachList) {
            String uploadPath = uploadRequestDto.getUploadPath().replace("\\", "/");
            uploadRequestDto.setUploadPath(uploadPath);
        }

        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts findPosts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다.id=" + id));

        if (findPosts.getUploadList().size() > 0) {
            List<Upload> uploadList = findPosts.getUploadList();
            uploadList.clear();

            List<PostsUploadRequestDto> attachList = requestDto.getAttachList();
            for (PostsUploadRequestDto uploadDto : attachList) {
                String replaceUploadPath = uploadDto.getUploadPath().replace("\\", "/");
                uploadDto.setUploadPath(replaceUploadPath);
                Upload upload = uploadDto.toEntity();
                findPosts.addUpload(upload);
            }
        }else{
            List<Upload> uploadList = findPosts.getUploadList();
            List<PostsUploadRequestDto> attachList = requestDto.getAttachList();
            for (PostsUploadRequestDto uploadDto : attachList) {
                String replaceUploadPath = uploadDto.getUploadPath().replace("\\", "/");
                uploadDto.setUploadPath(replaceUploadPath);
                Upload upload = uploadDto.toEntity();
                findPosts.addUpload(upload);
            }
        }

        findPosts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {

        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글은 업습니다. id=" + id));
        postsRepository.delete(posts);
    }

    @Transactional
    public int selectPostLike(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글은 업습니다. id=" + id));
        List<Like> likeList = posts.getLikeList();
        int postLike = 0;
        int postDislike = 0;

        for (Like likes : likeList) {
            postLike += likes.getLikes();
            postDislike += likes.getDislike();
        }

        if (postLike >= postDislike) {
            return postLike + postDislike;
        } else {
            return postDislike + postLike;
        }

    }

    @Transactional
    public int insertPostLike(Long id, SessionUser user, PostsLikeRequestDto postsLikeRequestDto) {

        //한번 누른사람 다시 못누르게하기
        List<Like> whoClickLike = likeRepository.findWhoClickLike(user.getEmail(), id);
        int totalDislike = 0;
        int totalLike = 0;
        int total = 0;
        boolean passInsert = false;
        if (whoClickLike.size() > 0) {

            for (Like postLike : whoClickLike) {
                totalDislike += postLike.getDislike();
                totalLike += postLike.getLikes();
            }

            log.info("totalLike :" + totalLike);
            log.info("totalDislike :" + totalDislike);

            if (totalLike >= totalDislike) {
                total = totalLike + totalDislike;
            }  else {
                total = totalDislike + totalLike;
            }

            if(total == 1 && postsLikeRequestDto.getDislike() == -1 ){
                passInsert = true;
            }else if(total == -1 && postsLikeRequestDto.getLikes() == 1){
                passInsert = true;
            }else if(total == 1){
                return 0;
            }else if(total == -1){
                return -1;
            }else if(total == 0){
                passInsert = true;
            }

        }else{
            passInsert = true;
        }

        if (passInsert) {
            Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글은 업습니다. id=" + id));
            User userNowInfo = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new IllegalArgumentException("해당 아이디는 업습니다. id=" + id));

            Like like = postsLikeRequestDto.toEntity();
            like.setPosts(posts);
            like.setUser(userNowInfo);

            likeRepository.save(like);
            em.flush();
            em.clear();

        }

        //return
        if(total == 1 && postsLikeRequestDto.getDislike() == -1 ){
            return 3;
        }else if(total == -1 && postsLikeRequestDto.getLikes() == 1){
            return 4;
        }else if(postsLikeRequestDto.getLikes() == 1){
            return 1;
        }else{
            return 2;
        }


    }
}
