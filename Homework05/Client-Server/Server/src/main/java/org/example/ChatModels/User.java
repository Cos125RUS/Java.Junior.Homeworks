package org.example.ChatModels;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ChatsList")
    private String chatsList;

    @Column(name = "Name")
    private String name;

    @Transient
    private ChatsList chats;
    @Transient
    private static long nextId = 1;

    public User() {
        chats = new ChatsList();
    }

    public User(String name) {
        this.id = nextId++;
        this.name = name;
        this.chats = new ChatsList();
        this.chatsList = "";
    }

    public void addChat(Chat chat) {
        chats.add(chat);
        updateChatsList();
    }

    public void delChat(Chat chat){
        chats.remove(chat);
        updateChatsList();
    }

    public void updateChatsList() {
        chatsList = chats.toDB();
    }


//region GettersAndSetters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatsList() {
        return chatsList;
    }

    public void setChatsList(String chatsList) {
        this.chatsList = chatsList;
    }

    public ChatsList getChats() {
        return chats;
    }
    //    endregion
}
