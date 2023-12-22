package org.example.ChatModels;

import javax.persistence.*;

@Entity
@Table(name = "Contacts")
public class Contact extends Chat{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatID;
    private User u1;
    @Column(name = "user1")
    private long u1ID;
    private User u2;
    @Column(name = "user2")
    private long u2ID;

    public Contact() {
    }

    public Contact(User u1, User u2) {
        this.chatID = generateID();
        this.u1 = u1;
        this.u1ID = u1.getId();
        this.u2 = u2;
        this.u2ID = u2.getId();
    }

    private long generateID() {
        return 0;
    }

//    region GettersAndSetters
    public long getChatID() {
        return chatID;
    }

    public void setChatID(long chatID) {
        this.chatID = chatID;
    }

    public long getU1ID() {
        return u1ID;
    }

    public void setU1ID(long u1ID) {
        this.u1ID = u1ID;
    }

    public long getU2ID() {
        return u2ID;
    }

    public void setU2ID(long u2ID) {
        this.u2ID = u2ID;
    }

    public User getU1() {
        return u1;
    }

    public void setU1(User u1) {
        this.u1 = u1;
    }

    public User getU2() {
        return u2;
    }

    public void setU2(User u2) {
        this.u2 = u2;
    }
//    endregion
}
