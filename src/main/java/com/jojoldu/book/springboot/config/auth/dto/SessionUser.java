package com.jojoldu.book.springboot.config.auth.dto;

import com.jojoldu.book.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    /**
     * SessionUser에는 [ 인증된 사용자 정보 ] 만 필요하다.
     * 그 외에는 필요한 정보가 없으니 name, email, picture만 필드로 선언한다.
     */

    private String name;
    private String email;
    private String picture;

    // 이게 직렬화?
    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }


}
