package com.example.api.request;

import org.springframework.web.multipart.MultipartFile;

public class EditProfileRequest {

    private MultipartFile photo;
    private String name;
    private String email;
    private String password;
    private int removePhoto;

    public EditProfileRequest(MultipartFile photo, String name, String email, String password, int removePhoto) {
        this.photo = photo;
        this.name = name;
        this.email = email;
        this.password = password;
        this.removePhoto = removePhoto;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRemovePhoto() {
        return removePhoto;
    }

    public void setRemovePhoto(int removePhoto) {
        this.removePhoto = removePhoto;
    }

    /*{"photo": <binary_file>,"name":"Sendel","email":"sndl@mail.ru","password":"123456","removePhoto":0}*/

}
