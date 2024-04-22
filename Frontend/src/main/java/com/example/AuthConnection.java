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
import org.json.*;

public class AuthConnection {
     public record LoginResponse(
        boolean success, String authtoken, String message
     ){}
    public record RefreshResponse(
       boolean success
    ){}

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public LoginResponse signup(String user, String email, String password) throws Exception {
        String response = sendPost("http://localhost:5000/signup",Map.of("user",user,"email",email,"password",password), " ");
        JSONObject jo = new JSONObject(response);
        String authtoken = null;
        String message = null;
        if(jo.has("auth-token")){
            authtoken = jo.getString("auth-token");
        }
        else{
            message = jo.getString("message");
        }
        return new LoginResponse(jo.getBoolean("success"),authtoken,message);
    }
    public LoginResponse login(String useremail, String password) throws Exception {
        String response = sendPost("http://localhost:5000/login",Map.of("useremail",useremail,"password",password), " ");
        JSONObject jo = new JSONObject(response);
        String authtoken = null;
        String message = null;
        if(jo.has("auth-token")){
            authtoken = jo.getString("auth-token");
        }
        else{
            message = jo.getString("message");
        }
        return new LoginResponse(jo.getBoolean("success"),authtoken,message);
    }

    public void logout(String authtoken) throws Exception {
        sendPost("http://localhost:5000/logout",new HashMap<>(),authtoken);

    }

    public RefreshResponse refreshToken(String authtoken) {
        try {
            String response = sendPost("http://localhost:5000/refresh_token", new HashMap<>(), authtoken);
            JSONObject jo = new JSONObject(response);
            return new RefreshResponse(jo.getBoolean("success"));
        }
        catch (Exception e){;
            return new RefreshResponse(false);
        }
    }

//    private String sendGet(String url) throws Exception {
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .GET()
//                .uri(URI.create(url))
//                .setHeader("User-Agent", "Java 11 HttpClient Bot")
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//        return response.body();
//
//    }

    private String sendPost(String url, Map<Object,Object> data , String header) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create(url))
                .setHeader("auth-token", header) // add request header
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        JSONObject jo = new JSONObject(data);
        return HttpRequest.BodyPublishers.ofString(jo.toString());
    }


}
