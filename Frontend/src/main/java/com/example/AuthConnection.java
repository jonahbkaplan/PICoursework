package com.example;


import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class AuthConnection {
     public record LoginResponse(
        boolean success, String authtoken, String message
     ){}
    public record RefreshResponse(
       boolean success, String message
    ){}




    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();




    public LoginResponse signup(String user, String email, String password) throws Exception {
        String response = sendPost("http://localhost:5000/signup",Map.of("user",user,"email",email,"password",password), " ");
        LoginResponse result =  new LoginResponse(false,null,null);
        return result;
    }
    public LoginResponse login(String useremail, String password) throws Exception {
        String response = sendPost("http://localhost:5000/login",Map.of("useremail",useremail,"password",password), " ");
        LoginResponse result =  new LoginResponse(false,null,null);
        return result;
    }

    public void logout(String authtoken) throws Exception {
        sendPost("http://localhost:5000/logout",new HashMap<>(),authtoken);

    }

    public RefreshResponse refreshToken(String authoken,String useremail, String password) throws Exception{
        String response = sendPost("http://localhost:5000/login",Map.of("useremail",useremail,"password",password), " ");
        RefreshResponse result =  new RefreshResponse(false,null);
        return result;
    }

    private String sendGet(String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

    private String sendPost(String url, Map<Object,Object> data , String header) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create(url))
                .setHeader("User-Agent", header) // add request header
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (!builder.isEmpty()) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }


}
