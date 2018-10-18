package hu.elte.go.dtos;

public class MessageDTO {

    private final String message;
    
    public MessageDTO(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

}
