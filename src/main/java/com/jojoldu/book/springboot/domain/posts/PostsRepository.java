package com.jojoldu.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Repository 어노테이션이 필요없다. - 컴포넌트 스캔을 spring data jpa가 자동으로 처리
 * 주의! Entity클래스와 EntityRepository는 함께 위치해야 한다.
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("select p from Posts p order by p.id desc")
    List<Posts> findAllDesc();

}


/*
    규모가 있는 프로젝트에서의 조회는 FK의 조인,
    복잡한 조건 등으로 인해 이런 Entity 클래스만으로 처리하기 어려워 조회용 프레임워크를 추가로 사용한다.
    (QueryDSL, jooq, MyBatis)

    [조회]는 위 세가지 프레임워크중 하나를 사용하고,
    [등록/수정/삭제] 등은 SpringDataJpa를 통해 진행한다.

    QueryDSL을 추천하는 이유

    1. 타입 안정성이 보장

        단순한 문자열로 쿼리를 생성하는게 아니라, method를 기반으로 쿼리를 생성하기 떄문에 컴파일 오류덕을 볼 수 있다.

    2. 국내 많은 회사에서 사용중이다.

    3. 레퍼런스가 많다.
        많은 회사와 개발자들이 사용하다보니 그만큼 국내 자료가 많다.




 */