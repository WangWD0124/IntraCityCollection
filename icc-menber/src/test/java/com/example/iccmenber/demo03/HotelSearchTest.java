package com.example.iccmenber.demo03;

import com.alibaba.fastjson.JSON;
import com.example.iccmenber.demo03.pojo.HotelDoc;
import com.example.iccmenber.demo03.service.IHotelSerice;
import org.apache.http.HttpHost;
import org.apache.lucene.index.Term;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HotelSearchTest {

    @Autowired
    private IHotelSerice hotelService;

    private RestHighLevelClient client;

    @Before
    public void setUp(){
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.145.101:9200")
        ));
    }

    @Test
    public void testMatchAll() throws IOException {
        //准备请求
        SearchRequest request = new SearchRequest("hotel");
        //准备
        request.source().query(QueryBuilders.matchAllQuery());
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应
        SearchHits searchHits = response.getHits();
        //获取条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+total+"条数据");
        //获取数据遍历
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits){
            String json = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            System.out.println("hotelDoc = "+hotelDoc);
        }
    }

    @Test
    public void testMatch() throws IOException {
        //准备请求
        SearchRequest request = new SearchRequest("hotel");
        //准备
        request.source().query(QueryBuilders.matchQuery("all", "如家"));
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应
        SearchHits searchHits = response.getHits();
        //获取条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+total+"条数据");
        //获取数据遍历
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits){
            String json = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            System.out.println("hotelDoc = "+hotelDoc);
        }
    }

    @Test
    public void testTermAndRangeWithBoolQuery() throws IOException {
        //准备请求
        SearchRequest request = new SearchRequest("hotel");
        //准备
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("city", "上海"));
        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(600));

        request.source().query(boolQuery);
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应
        SearchHits searchHits = response.getHits();
        //获取条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+total+"条数据");
        //获取数据遍历
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits){
            String json = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            System.out.println("hotelDoc = "+hotelDoc);
        }
    }

    @Test
    public void testPageAndSort() throws IOException {
        int page = 1, size = 5;
        //准备请求
        SearchRequest request = new SearchRequest("hotel");
        //准备
        request.source().query(QueryBuilders.matchAllQuery());
        request.source().sort("price", SortOrder.ASC);
        request.source().from((page-1)*size).size(size);
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应
        SearchHits searchHits = response.getHits();
        //获取条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+total+"条数据");
        //获取数据遍历
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits){
            String json = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            System.out.println("hotelDoc = "+hotelDoc);
        }
    }

    @Test
    public void testHighlight() throws IOException {
        //准备请求
        SearchRequest request = new SearchRequest("hotel");
        //准备
        request.source().query(QueryBuilders.matchQuery("all", "如家"));
        request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));

        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应
        SearchHits searchHits = response.getHits();
        //获取条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+total+"条数据");
        //获取数据遍历
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits){
            String json = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            System.out.println("hotelDoc = "+hotelDoc);
        }
    }

    @Test
    public void testAggregation() throws IOException {
        //准备请求
        SearchRequest request = new SearchRequest("hotel");
        //准备
        request.source().size(0);
        request.source().aggregation(AggregationBuilders
                .terms("brand_agg")
                .field("brand")
                .size(20)
                .subAggregation(
                        AggregationBuilders
                                .count("score_count")
                                .field("score")
                )
                .subAggregation(
                        AggregationBuilders
                                .max("score_max")
                                .field("score")
                )
                .subAggregation(
                        AggregationBuilders
                                .min("score_min")
                                .field("score")
                )
                .subAggregation(
                        AggregationBuilders
                                .avg("score_avg")
                                .field("score")
                )
                .subAggregation(
                        AggregationBuilders
                                .sum("score_sum")
                                .field("score")
                ));

        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        //解析响应
        Aggregations aggregations = response.getAggregations();
        //获取条数
        Terms brandTerms = aggregations.get("brand_agg");
        //获取数据遍历
        List<? extends Terms.Bucket> buckets = brandTerms.getBuckets();
        for (Terms.Bucket bucket : buckets){
            String brandName = bucket.getKeyAsString();
            long count = bucket.getDocCount();
            Max score_max = bucket.getAggregations().get("score_max");
            Min score_min = bucket.getAggregations().get("score_min");
            Avg score_avg = bucket.getAggregations().get("score_avg");
            Sum score_sum = bucket.getAggregations().get("score_sum");
            System.out.println("对品牌聚合查询得出统计数据："
                    +brandName +"-"
                    +count+"-"
                    +score_sum.getValue()+"-"
                    +score_max.getValue()+"-"
                    +score_min.getValue()+"-"
                    +score_avg.getValue());
        }
    }

    @Test
    public void testAggregationMap() throws IOException {
        //准备请求
        SearchRequest request = new SearchRequest("hotel");
        request.source().size(0);
        buildAggSearchRequest(request, "brand_agg", "brand", 20);
        buildAggSearchRequest(request, "city_agg", "city", 20);
        buildAggSearchRequest(request, "star_agg", "starName", 20);
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应
        Aggregations aggregations = response.getAggregations();

        Map<String, List<String>> aggMap = new HashMap<>();
        aggMap.put("brand_agg", getAggList(aggregations, "brand_agg"));
        aggMap.put("city_agg", getAggList(aggregations, "city_agg"));
        aggMap.put("star_agg", getAggList(aggregations, "star_agg"));

        System.out.println(aggMap.toString());

    }

    private void buildAggSearchRequest (SearchRequest request, String aggName, String field, int size) {

        //准备

        request.source().aggregation(AggregationBuilders
                .terms(aggName)
                .field(field)
                .size(size));
    }

    private List<String> getAggList(Aggregations aggregations, String aggName) throws IOException {

        Terms terms = aggregations.get(aggName);
        //获取数据遍历
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        List<String> keyNames = new ArrayList<>();
        for (Terms.Bucket bucket : buckets){
            String keyName = bucket.getKeyAsString();
            keyNames.add(keyName);
        }
        return keyNames;
    }

    @Test
    public void testSuggest() throws IOException {
        SearchRequest request = new SearchRequest();
        request.source().suggest(new SuggestBuilder().addSuggestion("mySuggestion",
                SuggestBuilders.completionSuggestion("enter")
                        .prefix("h")
                        .skipDuplicates(true)
                        .size(10)));
        client.search(request, RequestOptions.DEFAULT);
    }


    @After
    public void tearDown() throws IOException {
        this.client.close();
    }
}
