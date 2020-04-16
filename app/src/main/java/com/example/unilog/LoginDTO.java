package com.example.unilog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class LoginDTO implements Serializable {
    private int id;
    private String token;
    private int role;
    private String email;
    private List<Integer> listApplicattion;
    private List<Integer> listApplicationInstance;

    public LoginDTO(int id, String token, int role, String email, List<Integer> listApplicattion, List<Integer> listApplicationInstance) {
        this.id = id;
        this.token = token;
        this.role = role;
        this.email = email;
        this.listApplicattion = listApplicattion;
        this.listApplicationInstance = listApplicationInstance;
    }

    public LoginDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Integer> getListApplicattion() {
        return listApplicattion;
    }

    public void setListApplicattion(List<Integer> listApplicattion) {
        this.listApplicattion = listApplicattion;
    }

    public List<Integer> getListApplicationInstance() {
        return listApplicationInstance;
    }

    public void setListApplicationInstance(List<Integer> listApplicationInstance) {
        this.listApplicationInstance = listApplicationInstance;
    }
}
