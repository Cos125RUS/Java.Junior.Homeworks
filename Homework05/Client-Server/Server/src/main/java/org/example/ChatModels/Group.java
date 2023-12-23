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

    public Group() {
    }

    public Group(String name, UsersList users) {
        super(users);
        this.id = super.getId();
        this.name = name;
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
        super.setId(id);
    }

//    endregion


    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", usersList='" + usersList + '\'' +
                ", users=" + users +
                '}';
    }
}
