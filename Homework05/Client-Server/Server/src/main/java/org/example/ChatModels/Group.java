package org.example.ChatModels;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "`Groups`")
public class Group extends Chat{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "Name")
    private String name;
    @Column(name = "UsersList")
    private String usersList;
    @Transient
    private UsersList users;

    public Group() {
    }

    public Group(String name, UsersList users) {
        super();
        this.id = super.getId();
        this.name = name;
        this.users = users;
    }
    public void addChat(User user) {
        users.add(user);
        updateChatsList();
    }

    public void delChat(User user){
        users.remove(user);
        updateChatsList();
    }

    public void updateChatsList() {
        usersList = users.toString();
    }

//    region GettersAndSetters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(UsersList users) {
        this.users = users;
    }
//    endregion
}
