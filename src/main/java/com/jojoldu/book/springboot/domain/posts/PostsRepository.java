package com.jojoldu.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Repository 어노테이션이 필요없다. - 컴포넌트 스캔을 spring data jpa가 자동으로 처리
 * 주의! Entity클래스와 EntityRepository는 함께 위치해야 한다.
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {

}
