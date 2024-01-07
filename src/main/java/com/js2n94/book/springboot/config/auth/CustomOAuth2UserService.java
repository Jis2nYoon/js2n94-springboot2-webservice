package com.js2n94.book.springboot.config.auth;

import com.js2n94.book.springboot.config.auth.dto.SessionUser;
import com.js2n94.book.springboot.domain.user.User;
import com.js2n94.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); 
        // 현재 로그인 진행 중인 서비스를 구분하는 코드입니다.
        // 지금은 구글만 사용하는 불필요한 값이지만, 이후 네이버 로그인 연동 시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용합니다.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); 
        // OAuth2 로그인 진행 시 키가 되는 필드값을 이야기합니다. Primary Key와 같은 의미입니다.
        // 구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본 지원하지 않습니다. 구글의 기본 코드는 "sub" 입니다.
        // 이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용됩니다. 
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes()); // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        
        User user = this.saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUser(user));
        /*
        - 세션에 사용자 정보를 저장하기 위한 Dto 클래스입니다.
        - 왜 User 클랙스를 사용하지 않고, 새로 만들어서 쓰는가?
            => User 클래스를 그대로 사용하면 다음과 같은 에러가 발생.
                Failed to convert from type [java.lang.Object] to type [byte[]] for value (User클래스명)
        - 이는 세션에 저장하기 위해, User 클래스를 세션에 저장하려고 하니, User클래스에 직렬화를 구현하지 않았다는 의미의 에러입니다.
        - 그럼 오류를 해결하기 위해 User 클래스에 직렬화 코드를 넣으면 될까요? 그것에 대해선 생각해 볼 것이 많습니다.
        - 이유는 User 클래스가 엔티티이기 때문입니다. 엔티티 클래스에는 언제 다른 엔티티와 관계가 형성될 지 모릅니다.
        - 예를 들어 @OneToMany, @ManyToMany 등 자식 엔티티를 갖고 있다면 직렬화 대상에 자식들까지 포함되니 성능 이슈, 부수효과가 발생할 확률이 높습니다.
        - 그래서 직렬화 기능을 가진 세션 Dto를 하나 추가로 만드는 것이 이후 운영 및 유지보수 때 많은 도움이 됩니다.
         */
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }

}
