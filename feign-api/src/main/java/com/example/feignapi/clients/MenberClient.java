package com.example.feignapi.clients;

import com.example.feignapi.clients.fallback.MenberClientFallbackFactory;
import com.example.feignapi.pojo.Menber;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "icc-menber", fallbackFactory = MenberClientFallbackFactory.class)
public interface MenberClient {

    @GetMapping
    Menber findById(@PathVariable("id") Long id);


}
