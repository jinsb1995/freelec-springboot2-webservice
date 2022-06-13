package com.jojoldu.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** @SpringBootApplication
 * - 스프링 부트의 자동 설정
 * - 스프링 Bean 읽기와 생성 등 모두 자동으로 설정
 * - @SpringBootApplication이 있는 위치부터 읽기 때문에 이 클래스는 항상 프로젝트의 최상단에 위치해야만 한다.
 */
@EnableJpaAuditing
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        /**
         * SpringApplication.run()를 이용해 내장 톰캣을 실행한다.
         * Jar - 실행 가능한 Java 패키징 파일
         * '언제 어디서나 같은 환경에서 스프링 부트를 배포할 수 있게 하기위해 내장 톰캣을 사용한다.'
         */
        SpringApplication.run(Application.class, args);
    }

}
