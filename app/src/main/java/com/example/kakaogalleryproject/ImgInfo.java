package com.example.kakaogalleryproject;

public class ImgInfo{
    private int orgIdx;
    private String href = "";
    private String date = "";
    private String name = "";

    public ImgInfo(int orgIdx, String href, String date, String name) {
        this.orgIdx = orgIdx;
        this.href = href;
        this.date = date;
        this.name = name;
    }
    public String getHref() {
        return this.href;
    }
    public int getOrgIdx() {
        return this.orgIdx;
    }
    public String getDate() {
        return this.date;
    }
    public String getName() {
        return this.name;
    }
}
