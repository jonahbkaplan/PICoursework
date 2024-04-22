package com;

import com.example.AuthConnection;

import java.io.FileReader;
import java.io.FileWriter;

public class AuthToken {
    private String token;
    private AuthConnection auth = new AuthConnection();

    public boolean getTokenFromFile() {
        try {
            FileReader reader = new FileReader("token.txt");
            char[] tokenBuf = new char[60];
            reader.read(tokenBuf);
            token = new String(tokenBuf);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean setToken(String token){
        try {
            FileWriter writer = new FileWriter("token.txt");
            writer.write(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public boolean refreshToken(){
        AuthConnection.RefreshResponse response = auth.refreshToken(token);
        return true;
    }
    
}
