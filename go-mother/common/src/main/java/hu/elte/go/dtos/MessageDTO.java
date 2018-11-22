package hu.elte.go.dtos;

import hu.elte.go.events.MessageEvent;

public class MessageDTO {

    private String message;
    
    public MessageDTO(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

    public MessageDTO(){
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
