package com.jojoldu.book.springboot.web;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 *  @WebMvcTest으로는, Controller, ControllerAdvice 등 [외부 연동과 관련된 부분만 활성화가 된다.]
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }


    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                    get("/helloV2")
                    .param("name", name)
                    .param("amount", String.valueOf(amount))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));

        /*
            jsonPath
                - JSON 응답값을 필드별로 검증할 수 있는 메소드
                - $ 를 기준으로 필드명을 명시한다.
                - 여기서는 name과 amount를 검증하니 $.name, $.amount로 검증한다.
         */
    }


/*

    1. @RunWith(SpringRunner.class)
        - 테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킨다.
        - 여기서는 SpringRunner라는 스프링 실행자를 실행한다.
        - springBoot와 JUnit 사이에 다리(연결자) 역할을 해준다.

    2. @WebMvcTest
        - 여러 스프링 test annotation중, Web(Spring Mvc)에 집중할 수 있는 어노테이션
        - 선언할 경우 @Controller, @ControllerAdvice 등을 사용할 수 있다.
        - 단 @Service, @Component, @Repository 등은 사용할 수 없다.
        - 여기서는 controller만 사용하기 때문에 선언한다.

    3. private MockMvc mvc;
        - 웹 API를 테스트할 때 사용한다.
        - 스프링 MVC 테스트의 시작점이다.
        - 이 클래스를 통해 HTTP Get, Post 등에 대한 API 테스트를 할 수 있다.


    4. mvc.perform(get("/hello"))
        - MockMvc를 통해 /hello 주소로 Http Get 요청을 보낸다.
        - 체이닝이 지원되어, andExpect와 같은 여러 검증 기능을 이어서 선언할 수 있다.

    5. .andExpect(status().isOk())
        - mvc.perform의 결과를 검증한다.
        - Http Header의 Status를 검증한다. -  200, 404, 500 등

    6. andExpect(content().String(hello))
        - mvc.perform의 결과를 검증한다.
        - response 본문의 내용을 검증한다.




 */



}