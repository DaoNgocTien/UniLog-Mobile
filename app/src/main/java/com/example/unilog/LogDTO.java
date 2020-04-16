package com.example.unilog;

import java.io.Serializable;

public class LogDTO implements Serializable {
    private int id;
    private String app_code;
    private String file_name;
    private String line_code;
    private String log_date;
    private String message;
    private String project_name;

    public LogDTO(int id, String app_code, String file_name, String line_code, String log_date, String message, String project_name) {
        this.id = id;
        this.app_code = app_code;
        this.file_name = file_name;
        this.line_code = line_code;
        this.log_date = log_date;
        this.message = message;
        this.project_name = project_name;
    }

    public LogDTO( ) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getLine_code() {
        return line_code;
    }

    public void setLine_code(String line_code) {
        this.line_code = line_code;
    }

    public String getLog_date() {
        return log_date;
    }

    public void setLog_date(String log_date) {
        this.log_date = log_date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
}
