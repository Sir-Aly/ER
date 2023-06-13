package com.example.easyreach;

import java.util.Map;

public class ChatRoom {
    private Map<String, Boolean> participants;

    public ChatRoom() {
        // Empty constructor needed for Firestore
    }

    public ChatRoom(Map<String, Boolean> participants) {
        this.participants = participants;
    }

    public Map<String, Boolean> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, Boolean> participants) {
        this.participants = participants;
    }
}
