package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<Group> groups = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public boolean addGroup(Group group) {
        return groups.add(group);
    }

    public boolean removeGroup(Group group) {
        return groups.remove(group);
    }
}
