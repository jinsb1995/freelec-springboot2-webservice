package com.jojoldu.book.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 응답 용도로 사용할 응답 객체
 */
@Getter
@RequiredArgsConstructor
public class HelloResponseDTO {
    
    private final String name;
    private final int amount;
    
}
