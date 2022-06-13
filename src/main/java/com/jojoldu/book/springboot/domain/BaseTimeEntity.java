package com.jojoldu.book.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * java8이 나오기 전 까지 사용되었던 Date와 Calendat 클래스는 다음과 같은 문제점들이 있었다.
 *
 * 1. 불변(변경이 불가능한) 객체가 아니다.
 *    - 멀티스레스 환경에서 문제가 발생할 수 있음
 *
 * 2. Calendat는 월(month) 값 설계가 잘못되었다.
 *    - 10월을 나ㄴ타내는 Calendar.OCTOBER의 숫자 값은 '9'이다.
 *    - 당연히 10으로 생각했던 사람들은 혼란스럽다.
 *
 * JodaTime이라는 오픈소스를 사용해서 문제점들을 해결했고,
 * java8에선 LocalDate를 통해 해결했다.
 *
 * 모든 Entity들의 상위 클래스가 되어 날짜를 관리해준다.
 *
 */
@Getter
@MappedSuperclass              // 1
@EntityListeners(AuditingEntityListener.class)   // 2
public class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;



/*
    1. MappedSuperclass
        JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들(createdDate, ModifiedDate)도 column으로 자동으로 인식하도록 한다.

    2. @EntityListeners(AuditingEntityListener.class)
        - BaseTimeEntity 클래스에 auditing기능을 포함시킨다.

 */

}
