package com.js2n94.book.springboot.web;

import com.js2n94.book.springboot.service.posts.PostsService;
import com.js2n94.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor  //이 어노테이션은 초기화되지 않은 final 필드나, @NonNull이 붙은 필드에 대해 생성자를 생성해줌. 주로 의존성 주입(Dependency Injection) 편의성을 위해서 사용. //Lombok 어노테이션임...
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts", postsService.findAllDesc());
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("posts", dto);
        return "posts-update";
    }

}
