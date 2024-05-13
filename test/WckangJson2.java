package com.lgcns.test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// JSON 구조를 표현하는 클래스
class Service {
    @SerializedName("target")
    private String target;

    @SerializedName("status")
    private String status;

    @SerializedName("services")
    private List<Service> services;

    public Service(String target, String status) {
        this.target = target;
        this.status = status;
        this.services = new ArrayList<>();
    }

    public void addTarget(String target) {
        this.target = target;
    }

    public void addService(Service service) {
        services.add(service);
    }

    public void addServiceUnderTarget(String target, Service service) {
        if (this.target.equals(target)) {
            services.add(service);
        } else {
            for (Service s : services) {
                s.addServiceUnderTarget(target, service);
            }
        }
    }
}

public class WckangJson2 {
    public static void main(String[] args) {
        // X-Caller 헤더 확인
        String xCallerHeader = ""; // 여기에 X-Caller 헤더 값을 얻어옴

        // 최초 호출인 경우
        if (xCallerHeader.isEmpty()) {
            Service rootService = new Service("http://127.0.0.1:5001/front", "200");

            // JSON 파일로 저장
            saveJson(rootService, "output.json");

            // HTTP 호출
            JettyClient.sendRequest("http://127.0.0.1:8081/front", "GET", "", "");
//            HttpService httpService = new HttpService("http://127.0.0.1:8081/front");
//            httpService.call();
        } else {
            // 이전 JSON 파일 로드
            Service rootService = loadJson("output.json");

            // 현재 호출된 주소 추가
            String currentTarget = ""; // 여기에 현재 호출된 주소를 받아옴
            Service newService = new Service(currentTarget, "200");
            rootService.addServiceUnderTarget(xCallerHeader, newService);

            // JSON 파일 업데이트
            saveJson(rootService, "output.json");

            // HTTP 호출
//            HttpService httpService = new HttpService(currentTarget);
//            httpService.call();
        }
    }

    // JSON 파일 저장
    private static void saveJson(Service service, String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(service);
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // JSON 파일 로드
    private static Service loadJson(String filename) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filename)) {
            return gson.fromJson(reader, Service.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
