package com.example.feignapi;

import com.example.feignapi.clients.fallback.MenberClientFallbackFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FeignApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignApiApplication.class, args);
    }

    @Bean
    public MenberClientFallbackFactory menberClientFallbackFactory(){
        return new MenberClientFallbackFactory();
    }
}
