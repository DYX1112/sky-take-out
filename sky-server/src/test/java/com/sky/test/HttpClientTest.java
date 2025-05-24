package com.sky.test;

import com.sky.utils.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-14
 * @Description: httpclient测试
 */
//@SpringBootTest
public class HttpClientTest {

    @Test
    public void testHttpClient() throws IOException {
        //创建client
        CloseableHttpClient aDefault = HttpClients.createDefault();

        //创建一个请求对象
        HttpGet httpGet =  new HttpGet("http://localhost:8080/user/shop/status");

        //发送请求
        CloseableHttpResponse response = aDefault.execute(httpGet);

        //拿到相应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        //获取请求体
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity);
        System.out.println(s);

        response.close();
        aDefault.close();
    }

    @Test
    public void testHttpClientPost() throws IOException, JSONException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","admin");
        jsonObject.put("password","123456");

        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        stringEntity.setContentEncoding("utf-8");
        stringEntity.setContentType("application/json");

        httpPost.setEntity(stringEntity);


        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        HttpEntity entity = httpResponse.getEntity();
        String s = EntityUtils.toString(entity);
        System.out.println(s);


        httpResponse.close();
        httpClient.close();
    }
}
