package com.leesungon.book.springboot.service.posts;

import com.leesungon.book.springboot.domain.posts.Posts;
import com.leesungon.book.springboot.domain.posts.upload.Upload;
import com.leesungon.book.springboot.domain.posts.upload.UploadRepository;
import com.leesungon.book.springboot.web.dto.PostsSaveRequestDto;
import com.leesungon.book.springboot.web.dto.PostsUploadRequestDto;
import lombok.Lombok;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j
public class UploadService {

    private final UploadRepository uploadRepository;

    @Transactional
    public List<Long> save(List<PostsUploadRequestDto> list) {

        List idList = new ArrayList();

        if(list.size()>0) {
            for (PostsUploadRequestDto postsUploadRequestDto : list) {
                Long id = uploadRepository.save(postsUploadRequestDto.toEntity()).getId();
                idList.add(id);
            }
        }

        return idList;
    }

    @Transactional
    public void deleteUpload(Long id) {
        Upload upload = uploadRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 파일은 업습니다. id="+id));
        uploadRepository.delete(upload);
    }
}