package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.instana.sdk.annotation.Span;
import com.instana.sdk.support.SpanSupport;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@RestController
public class HelloController {
    private final String SPAN_NAME = "my-custom-http-server";

    @GetMapping("/hello")
    @Span(type = Span.Type.ENTRY, value = SPAN_NAME)
    public String hello() {
        // 觸發一個故意的錯誤
	System.out.println("Current span ID: " + Long.toHexString(SpanSupport.currentSpanId(Span.Type.ENTRY)));
        SpanSupport.annotate(Span.Type.ENTRY, SPAN_NAME, "tags.http.method", "GET");
        SpanSupport.annotate(Span.Type.ENTRY, SPAN_NAME, "tags.http.status_code", "200");
        SpanSupport.annotate(Span.Type.ENTRY, SPAN_NAME, "tags.error", "false");
       // int i = 1 / 0;
        //return "Hello, World!";
	CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
          HttpGet request = new HttpGet("http://10.107.85.88:8080/generate-error");
          CloseableHttpResponse response = httpClient.execute(request);
          BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
          return "Hello, " + reader.readLine() + "!";
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }
    @GetMapping("/generate-error")
    @Span(type = Span.Type.EXIT, value = "my-custom-http-server")
    public String generateError() {
	int i = 1 / 0;
        return "Hello, World!";
        //try {
            // 故意觸發錯誤
        //    int result = 1 / 0;
        //    return "Success"; // 這一行不會執行，因為錯誤會在上一行觸發
        //} catch (Exception e) {
            // 捕獲錯誤並打印錯誤訊息
        //    System.out.println("An error occurred: " + e.getMessage());
        //    return "Error occurred";
        //}
    }
}

