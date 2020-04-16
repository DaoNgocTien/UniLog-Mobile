package com.example.unilog;

import java.io.Serializable;

public class ApplicationInstanceDTO implements Serializable {
    private String name;
    private String app_code;
    private String release_url;

    public ApplicationInstanceDTO( ) {
    }

    public ApplicationInstanceDTO(String name, String app_code, String release_url) {
        this.name = name;
        this.app_code = app_code;
        this.release_url = release_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public String getRelease_url() {
        return release_url;
    }

    public void setRelease_url(String release_url) {
        this.release_url = release_url;
    }
}
