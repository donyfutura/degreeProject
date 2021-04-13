package com.example.api.response;

public class UserDTO {
    private int id;
    private String name;
    private String photo;

    public UserDTO(int id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public UserDTO() {
    }

    public UserDTO(int id, String name) {
        this.id = id;
        this.name = name;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
