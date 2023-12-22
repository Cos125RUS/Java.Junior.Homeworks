package org.example.ChatModels;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Groups")
public class Group extends Chat{
    @Column(name = "UserListID")
    private long userListID;

    @Column(name = "Name")
    private String name;
    private List<User> users;

    public Group() {
    }

    public Group(String name, List<User> users) {
        this.userListID = generateID();
        this.name = name;
        this.users = users;
    }

    private long generateID() {
        return 0;
    }

//    region GettersAndSetters
    public long getUserListID() {
        return userListID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserListID(long userListID) {
        this.userListID = userListID;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
//    endregion
}
