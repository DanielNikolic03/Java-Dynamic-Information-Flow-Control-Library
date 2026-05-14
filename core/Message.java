package core;

import lattice.Label;
import lattice.Labeled;

public class Message<T> {
    private final Labeled<T> payload;
    private final Label presenceLabel;
    private final String senderId;

    public Message(Labeled<T> payload, Label presenceLabel, String senderId) {
        this.payload = payload;
        this.presenceLabel = presenceLabel;
        this.senderId = senderId;
        
    }

    public Labeled<T> getPayload() {
        return payload;
    }

    public Label getPresenceLabel() {
        return presenceLabel;
    }
}