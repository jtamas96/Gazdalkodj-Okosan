package hu.elte.gazdalkodjokosan.model;

import javafx.util.StringConverter;

public class Room {
    public Room(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long id;
    String name;

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
