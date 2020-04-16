package com.example.unilog;

import java.io.Serializable;

public class ApplicationDTO implements Serializable {
    private int id;
    private String name;
    private int appInsCount;

    public ApplicationDTO(int id, String name, int appInsCount) {
        this.id = id;
        this.name = name;
        this.appInsCount = appInsCount;
    }

    public ApplicationDTO() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAppInsCount() {
        return appInsCount;
    }

    public void setAppInsCount(int appInsCount) {
        this.appInsCount = appInsCount;
    }
}
