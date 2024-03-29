package com.js2n94.book.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 각 사용자의 권한을 관리할 Enum 클래스 Role
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    /*
     *  스프링 시큐리티에서는 권한 코드에 항상 ROLE_이 앞에 있어야만 합니다. 그래서 코드별 키 값을 ROLE_GUEST, ROLE_USER 등으로 지정합니다.
     * 마지막으로 User의 CRUD를 책임질 UserRepository도 생성합니다.
     */

    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;

}
