package com.leesungon.book.springboot.domain.posts.upload;

import com.leesungon.book.springboot.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UploadRepository extends JpaRepository<Upload,Long> {

}
