package com.example.iccmenber.demo01;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/menber/provider")
public class PersonController {

    //实验：模拟返回查询数据
    @RequestMapping(value = "/person/{personId}", method = RequestMethod.GET)
    public String findName(@PathVariable("personId") Integer personId, HttpServletRequest request){
        Person person = new Person(personId, "TOM", 23);
        person.setMessage(request.getRequestURL().toString()); //保存请求的URL
        Map<String, String> paramterMap = Maps.newHashMap();
        paramterMap.put("PersonId", person.getPersonId().toString());
        paramterMap.put("Pname", person.getPname());
        paramterMap.put("Page", Integer.toString(person.getPage()));
        paramterMap.put("Message", person.getMessage());
        String str = JSON.toJSONString(paramterMap);
        System.out.println("provider：访问person成功！" + str);
        return str;
    }

    //实验：模拟返回查询数据
    @RequestMapping(value = "/person2", method = RequestMethod.GET)
    public String findName2(){
        return "请求2";
    }

    //实验5：feign服务端指定请求内容类型           consume是：指定请求内容类型；Produces是：指定返回内容类型。
    @RequestMapping(value = "/person/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createPerson(@RequestBody Person person){
        System.out.println("请求内容：" + person);
        return "success, person id:"+person.PersonId;
    }

}
