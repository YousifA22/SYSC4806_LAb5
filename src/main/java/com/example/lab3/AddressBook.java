package com.example.lab3;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AddressBook {
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BuddyInfo> myBuddies;
    @Id
    @GeneratedValue
    private Long id;

    public AddressBook() {

        myBuddies = new ArrayList<BuddyInfo>();
    }

    public void addBuddy(BuddyInfo aBuddy) {
        if (aBuddy != null) {
            myBuddies.add(aBuddy);
        }
    }//funny

    public void removeBuddy(BuddyInfo s) {
        myBuddies.remove(s);


    }

    public List<BuddyInfo> getBuddies(){
        return myBuddies;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
