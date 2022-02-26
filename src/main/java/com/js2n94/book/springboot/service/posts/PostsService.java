package com.js2n94.book.springboot.service.posts;

import com.js2n94.book.springboot.domain.posts.Posts;
import com.js2n94.book.springboot.domain.posts.PostsRepository;
import com.js2n94.book.springboot.web.dto.PostsListResponseDto;
import com.js2n94.book.springboot.web.dto.PostsResponseDto;
import com.js2n94.book.springboot.web.dto.PostsSaveRequestDto;
import com.js2n94.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    // 트랜잭션 범위를 조회 기능만 남겨두어, 조회 속도가 개선됨.
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        // PostsListResponseDto::new  // posts -> new PostsListResponseDto(posts)) // posts = new PostsListResponseDto(posts))
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        postsRepository.delete(posts); //JpaRepository에서 이미 delete 메소드를 지원함.
    }
}
