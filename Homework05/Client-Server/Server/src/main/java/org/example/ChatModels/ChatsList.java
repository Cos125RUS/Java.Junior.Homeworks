package org.example.ChatModels;

import java.util.ArrayList;

public class ChatsList extends ArrayList<Chat> {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size() - 1; i++) {
            sb.append(this.get(i).getId());
            sb.append(":");
        }
        sb.append(this.get(this.size() - 1));
        sb.append(":");
        return sb.toString();
    }
}
