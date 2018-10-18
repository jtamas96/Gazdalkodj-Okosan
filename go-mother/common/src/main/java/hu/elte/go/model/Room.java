package hu.elte.go.model;

import javafx.util.StringConverter;

public class Room {
    long id;
    String name;

    public Room(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static RoomStringConverter getStringConverter() {
        return new RoomStringConverter();
    }

    private static class RoomStringConverter extends StringConverter<Room> {
        @Override
        public String toString(Room object) {
            return object.getName();
        }

        @Override
        public Room fromString(String name) {
            return new Room(1L, name);
        }
    }
}
