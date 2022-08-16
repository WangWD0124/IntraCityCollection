package com.example.iccmenber.demo03;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static com.example.iccmenber.demo03.constants.HotelConstants.MAPPING_TEMPLATE;

public class HotelIndexTest {

    private RestHighLevelClient client;

    @Test
    public void testInit(){
        System.out.println(client);
    }
    @Before
    public void setUp(){
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.145.101:9200")
        ));
    }

    //测试创建索引库
    @Test
    public void testCreateHotelIndex() throws IOException {
        //创建Request对象
        CreateIndexRequest request = new CreateIndexRequest("hotel");
        //请求参数，静态常量字符串，内容是创建索引库的DSL语句
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        //发起请求
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    //测试删除索引库
    @Test
    public void testDeleteHotelIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("hotel");
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    //测试判断索引库是否存在
    @Test
    public void testExistHotelIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("hotel");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists ? "索引库已经存在！" : "索引库不存在！");
    }



    @After
    public void tearDown() throws IOException{
        this.client.close();
    }


}
