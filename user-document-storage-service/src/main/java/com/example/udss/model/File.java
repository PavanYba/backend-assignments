package com.example.udss.model;

import lombok.Getter;

@Getter
public class File {

    private String fileName;
    private String url;

    public File(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
