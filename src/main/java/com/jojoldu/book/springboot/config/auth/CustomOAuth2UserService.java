package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.config.auth.dto.OAuthAttributes;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.user.User;
import com.jojoldu.book.springboot.domain.user.UserRepository;
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

    /**
     * 이 클래스에서는 구글 로그인 이후 가져온 사용자의 정보(email, name, picture 등) 들을 기반으로
     * 가입 및 정보수정, 세션 저장 등의 기능을 지원한다.
     */

    private final UserRepository userRepository;

    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // delegate = 위임하다.
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**   registrationId
         * - 현재 로그인 진행중인 서비스를 구분하는 코드이다.
         * - 지금은 구글만 사용하는 불필요한 값이지만,
         *   이후 네이버 로그인 연동시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용한다.
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        /**   userNameAttributeName
         * - OAuth2 로그인 진행 시 키가 되는 필드값을 이야기한다. Primary Key와 같은 의미이다.
         * - 구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본지원하지 않는다.
         *   구글의 기본 코드는 [ sub ] 이다.
         * - 이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용된다.
         */
        String userNameAttributeName = userRequest.getClientRegistration()
                                                    .getProviderDetails()
                                                    .getUserInfoEndpoint()
                                                    .getUserNameAttributeName();


        /**   OAuthAttributes
         * - OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스이다.
         * - 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용한다.
         */
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        /**
         * 구글 사용자 정보가 업데이트 되었을 때를 대비하여 update 기능도 같이 구현했다.
         * 사용자의 이름(name)이나 사진(picture)이 변경되면 User 엔티티에도 반영된다. (더티체킹을 위해)
         */
        User user = saveOrUpdate(attributes);

        /**   SessionUser
         * - 세션에 사용자를 저장하기 위한 DTO 클래스이다.
         * - User 클래스는 Entity 클래스이기 때문에 보안문제로 인해 SessionUser클래스를 따로 만들어서 사용한다.
         * ** 맨 밑에 부연 설명
         */
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                                attributes.getAttributes(),
                                attributes.getNameAttributeKey()
                );

    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())  // ↓↓↓↓↓  find로 찾아서 있는데 수정사항이 있을경우 더티체킹을 이용해서 수정하고, 아님 그냥 저장한다.
                                    .map((entity) -> entity.update(attributes.getName(), attributes.getPicture()))
                                    .orElse(attributes.toEntity());  // 없으면 새로 만들어서 넣는다.

        return userRepository.save(user);
    }

}

/*


    왜 User 클래스를 사용하지 않고, SessionUser를 사용하는가?

        Failed to convert from type [java.lang.Object] to type [byte[]] for value com.jojoldu..User@4u12j4

        위의 오류는, 로그인 사용자 정보를 세션에 저장하기 위해
        User 클래스를 세션에 저장하려고 했을 때, User 클래스에 [ 직렬화를 구현하지 않았다. ]는 의미의 에러이다.

        그렇다고 이걸 해결하기 위해 User 클래스에 [직렬화 코드]를 넣다니, User 클래스는 [엔티티 클래스]이기 때문에 제한된다.

        엔티티 클래스에는 언제 다른 엔티티와 관계가 형성될지 모른다.

        ex)
            @OneToMany, @ManyToMany 등 자식 엔티티를 갖고 있다면,
            직렬화 대상에 [자식]들 까지 포함되니 [성능이슈, 부수 효과]가 발생할 확률이 높다.

        그래서 [직렬화 기능을 가진 세션 DTO]를 하나 추가 로 만드는 것이 이후 운영 및 유지보수 때 많은 도움이 된다.




 */