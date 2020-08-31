package com.leesungon.book.springboot.domain.posts.like;

import com.leesungon.book.springboot.domain.posts.upload.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import java.util.List;

public interface LikeRepository extends JpaRepository<Like,Long> {

    @Query("select l from Like l where l.user.email = :userEmail and l.posts.id = :postId")
    List<Like> findWhoClickLike(String userEmail,Long postId);
}
