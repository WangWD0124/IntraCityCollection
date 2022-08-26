package com.example.iccmenber.demo01;


import lombok.Data;

@Data//可省略setter、getter
public class Person {

    // 参数名一定要一致
    Integer PersonId;
    String Pname;
    int Page;
    String Message;//增加message属性，保存请求的URL

    public Person(Integer personId, String pname, int page) {
        PersonId = personId;
        Pname = pname;
        Page = page;
    }



}
