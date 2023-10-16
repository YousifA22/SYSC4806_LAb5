package com.example.lab3;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BuddyInfo {
    String name;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    private Long id;

    public BuddyInfo() {
    }
    public BuddyInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name=name;}

    public String toString(){

        return this.name;
    }



    public Long getId() {
        return id;
    }
}