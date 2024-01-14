package com.js2n94.book.springboot.web;

import com.js2n94.book.springboot.config.auth.LoginUser;
import com.js2n94.book.springboot.config.auth.dto.SessionUser;
import com.js2n94.book.springboot.service.posts.PostsService;
import com.js2n94.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor  //이 어노테이션은 초기화되지 않은 final 필드나, @NonNull이 붙은 필드에 대해 생성자를 생성해줌. 주로 의존성 주입(Dependency Injection) 편의성을 위해서 사용. //Lombok 어노테이션임...
@Controller
public class IndexController {

    private final PostsService postsService;

    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){
        // 기존에 httpSession.getAttribute("user")로 가져오던 세션 정보 값이 개선되었습니다. 이제는 어느 컨트롤러든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 되었습니다.
        model.addAttribute("posts", postsService.findAllDesc());

        //SessionUser user = (SessionUser) httpSession.getAttribute("user"); // 앞서 작성된 CustomOAuth2UserService에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성했습니다.
        // 즉, 로그인 성공 시 httpSession.getAttribute("user")에서 값을 가져올 수 있습니다.

        if (user != null){ // 세션에 저장된 값이 있을 때만 model에 userName으로 등록합니다.
            // 세션에 저장된 값이 없으면 model에 아무런 값이 없는 상태이니 로그인 버튼이 보이게 됩니다.
            model.addAttribute("userName", user.getName());
            model.addAttribute("userPicture", user.getPicture());
        }
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
