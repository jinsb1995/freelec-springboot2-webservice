package com.jojoldu.book.springboot.web.dto;

import com.jojoldu.book.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * - 이 클래스는 Posts Entity클래스와 거의 똑같다.
 * - Entity 클래스는 Request/Response 클래스로 사용해서는 안된다.
 * 그 이유는,
 *      Entity 클래스는 [데이터베이스와 맞닿은 핵심 클래스]이기 때문이다.
 *      Entity 클래스를 기준으로 테이블이 생성되고, 스키마가 변경된다.
 */
@Getter
@NoArgsConstructor
public class PostsSaveRequestDTO {

    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDTO(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }


}
