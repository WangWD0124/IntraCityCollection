package com.example.feignapi.clients.fallback;

import com.example.feignapi.clients.MenberClient;
import com.example.feignapi.pojo.Menber;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MenberClientFallbackFactory implements FallbackFactory<MenberClient> {
    @Override
    public MenberClient create(Throwable cause) {
        return new MenberClient() {
            @Override
            public Menber findById(Long id) {
                log.error("查询用户异常", cause);
                return null;
            }
        };
    }
}
