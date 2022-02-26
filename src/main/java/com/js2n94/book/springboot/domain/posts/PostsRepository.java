package com.js2n94.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    /*
    * querydsl, jooq, MyBatis
    *
    * SpringDataJpa 로 등록/수정/삭제를 진행하고, 조회는 위 프레임워크로 이용한다. 저자는 querydsl을 추천한다고 함.(쿠팡,배민에서 사용)
    * */

    // SpringDataJpa에서 제공하지 않는 메소드는 쿼리로 직접 작성해도 됨.
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

}
