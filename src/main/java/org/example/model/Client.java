package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Client {

    private long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<Group> groups = new ArrayList<>();

    public Client() {
    }

    public Client(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }

        Client client = (Client) o;
        if (id != client.getId()) {
            return false;
        }

        if (!getFirstName().equals(client.getFirstName())) {
            return false;
        }
        if (!getLastName().equals(client.getLastName())) {
            return false;
        }

        return getPhoneNumber().equals(client.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        int result = (int) id;
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getPhoneNumber().hashCode();
        return result;
    }
}
