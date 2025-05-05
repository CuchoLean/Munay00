package com.munay.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private int age;
    private long tel;
    private String bio;
    private List<String> fotosUrl;
    private List<String> likes;    // IDs de usuarios a los que le dio like
    private List<String> matches;  // IDs de matches confirmados

    // Constructor vacío requerido por Spring
    public Usuario() {
    }

    public Usuario(String id, String name, String email, String password, int age, long tel, String bio, List<String> fotosUrl, List<String> likes, List<String> matches) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.tel = tel;
        this.bio = bio;
        this.fotosUrl = fotosUrl;
        this.likes = likes;
        this.matches = matches;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getTel() {
        return tel;
    }

    public void setTel(long tel) {
        this.tel = tel;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getFotosUrl() {
        return fotosUrl;
    }

    public void setFotosUrl(List<String> fotosUrl) {
        this.fotosUrl = fotosUrl;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getMatches() {
        return matches;
    }

    public void setMatches(List<String> matches) {
        this.matches = matches;
    }
}

