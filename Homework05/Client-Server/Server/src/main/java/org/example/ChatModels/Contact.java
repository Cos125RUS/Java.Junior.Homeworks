package org.example.ChatModels;

import javax.persistence.*;

@Entity
@Table(name = "Contacts")
public class Contact extends Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user1")
    private long u1ID;
    @Column(name = "user2")
    private long u2ID;
    @Transient
    private User u1;
    @Transient
    private User u2;

    public Contact() {
    }

    public Contact(User u1, User u2) {
        super(u1, u2);
        this.id = super.getId();
        this.u1 = u1;
        this.u1ID = u1.getId();
        this.u2 = u2;
        this.u2ID = u2.getId();
    }

    //    region GettersAndSetters
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        super.setId(id);
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


    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", u1ID=" + u1ID +
                ", u2ID=" + u2ID +
                ", u1=" + u1 +
                ", u2=" + u2 +
                '}';
    }
}
