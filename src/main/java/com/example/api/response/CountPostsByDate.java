package com.example.api.response;


import java.util.Date;



public class CountPostsByDate {

    private String date;
    private Long count;

    public CountPostsByDate(String date, Long count) {
        this.date = date;
        this.count = count;
    }

    public CountPostsByDate() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
