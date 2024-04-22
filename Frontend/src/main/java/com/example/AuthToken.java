package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class AuthToken {
    private String token;
    private AuthConnection auth = new AuthConnection();

    public AuthToken() {
        getTokenFromFile();
    }

    public boolean getTokenFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("token.txt"));
            token = reader.readLine();
            reader.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean setToken(String tokenP){
        try {
            token = tokenP;
            FileWriter writer = new FileWriter("token.txt");
            writer.write(token);
            writer.close();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean refreshToken() {
        try {
            AuthConnection.RefreshResponse response = auth.refreshToken(token);
            return response.success();
        } catch (Exception e) {
            return false;
        }
    }
}
