package org.example.ChatModels;

import java.util.ArrayList;

public class UsersList extends ArrayList<User> {
    public String toDB() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size() - 1; i++) {
            sb.append(this.get(i).getId());
            sb.append("%");
        }
        sb.append(this.get(this.size() - 1).getId());
        return sb.toString();
    }
}
