package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.web.dto.HelloResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/helloV1")
    public String helloV1() {
        return "hello";
    }

    @GetMapping("/helloV2")
    public HelloResponseDTO helloV2(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        return new HelloResponseDTO(name, amount);
    }
}
