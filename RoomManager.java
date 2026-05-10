import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    private List<Room> rooms;

    public RoomManager() {
        rooms = new ArrayList<>();
        initializeDefaultRooms();
    }

    private void initializeDefaultRooms() {
        rooms.add(new Room("R101", "Single", 100.00, 1));
        rooms.add(new Room("R102", "Single", 100.00, 1));
        rooms.add(new Room("R201", "Double", 150.00, 2));
        rooms.add(new Room("R202", "Double", 150.00, 2));
        rooms.add(new Room("R301", "Suite", 250.00, 4));
        rooms.add(new Room("R302", "Suite", 250.00, 4));
    }

    public List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable()) {
                available.add(room);
            }
        }
        return available;
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public Room findRoomById(String roomId) {
        for (Room room : rooms) {
            if (room.getId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    public boolean isRoomAvailable(String roomId) {
        Room room = findRoomById(roomId);
        return room != null && room.isAvailable();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }
}