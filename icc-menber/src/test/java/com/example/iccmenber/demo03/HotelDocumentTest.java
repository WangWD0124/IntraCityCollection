package com.example.iccmenber.demo03;


import com.alibaba.fastjson.JSON;
import com.example.iccmenber.IccMenberApplication;
import com.example.iccmenber.demo03.pojo.Hotel;
import com.example.iccmenber.demo03.pojo.HotelDoc;
import com.example.iccmenber.demo03.service.IHotelSerice;
import org.apache.http.HttpHost;
import org.checkerframework.checker.units.qual.A;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.ml.GetRecordsRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IccMenberApplication.class)
public class HotelDocumentTest {

    @Autowired
    private IHotelSerice hotelService;

    private RestHighLevelClient client;

    @Before
    public void setUp(){
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.145.101:9200")
        ));
    }

    //测试创建文档
    @Test
    public void testAddDocument() throws IOException {
        //根据id查询酒店数据
        Hotel hotel = hotelService.getById(61083L);
        //转换为文档格式
        HotelDoc hotelDoc = new HotelDoc(hotel);
        //将HotelDoc转json
        String json = JSON.toJSONString(hotelDoc);
        System.out.println(json);

        //创建请求，指定索引库和文档id
        IndexRequest request = new IndexRequest("hotel").id(hotelDoc.getId().toString());
        //请求绑定文档内容和文档格式
        request.source(json, XContentType.JSON);
        //发送请求
        client.index(request, RequestOptions.DEFAULT);
    }

    //测试查询文档
    @Test
    public void testGetDocumentById() throws IOException {
        //创建查询请求，指定索引库和文档id
        GetRequest request = new GetRequest("hotel", "61083");
        //发送请求并接收响应
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        //获取响应的字符串数据，并转换为json，进而转换为文档格式
        String json = response.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);

        System.out.println(hotelDoc);
    }

    //测试删除文档
    @Test
    public void testDelteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("hotel", "61083");
        client.delete(request, RequestOptions.DEFAULT);
    }

    //测试修改文档
    @Test
    public void testUpdateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("hotel", "61083");
        request.doc(
                "price", "999",
                "starName", "四钻"
        );
        client.update(request, RequestOptions.DEFAULT);
    }

    //测试批量导入
    @Test
    public void testBulkRequest() throws IOException {
        List<Hotel> hotels = hotelService.list();
        BulkRequest request = new BulkRequest();
        for (Hotel hotel : hotels){
            HotelDoc hotelDoc = new HotelDoc(hotel);
            request.add(new IndexRequest("hotel")
                    .id(hotelDoc.getId().toString())
                    .source(JSON.toJSONString(hotelDoc), XContentType.JSON));
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }


    @After
    public void tearDown() throws IOException{
        this.client.close();
    }




}
