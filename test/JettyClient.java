package com.lgcns.test;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.client.util.StringContentProvider;

class JettyClient {
    public static ContentResponse sendRequest(String url, String method, String apiKey, String requestBody) {
        try {
            // Jetty HttpClient 인스턴스 생성
            HttpClient httpClient = new HttpClient();
            // HttpClient 초기화
            httpClient.start();

            // 요청 생성
            Request request = httpClient.newRequest(url);
            					//.timeout(5, TimeUnit.SECONDS) // 타임아웃 설정

            // 메서드 설정
            request.method(HttpMethod.fromString(method.toUpperCase()));

            // 헤더 추가
            if (apiKey != null && !apiKey.isEmpty()) {
                request.header("X-API", apiKey);
            }

            // 요청 바디 설정
            if (requestBody != null && !requestBody.isEmpty()) {
                // 요청 본문이 있는 경우에만 설정
                request.content(new StringContentProvider(requestBody), "application/json");
            }

            // 요청 전송 및 응답 받기
            ContentResponse response = request.send();

            // HttpClient 종료
            httpClient.stop();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
