package com.example.api.response;

import java.util.HashMap;
import java.util.List;

public class CalendarResponse {

    private List<Integer> years;
    private HashMap<String, Long> posts;

    public CalendarResponse(List<Integer> years, HashMap<String, Long> posts) {
        this.years = years;
        this.posts = posts;
    }

    public CalendarResponse() {

    }

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public HashMap<String, Long> getPosts() {
        return posts;
    }

    public void setPosts(HashMap<String, Long> posts) {
        this.posts = posts;
    }
}
