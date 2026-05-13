import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomManager {
    private List<Room> rooms;
    private final String DATA_FILE = "data/rooms.txt";

    public RoomManager() {
        rooms = new ArrayList<>();
        loadFromFile();
        if (rooms.isEmpty()) {
            initializeDefaultRooms();
        }
    }

    private void initializeDefaultRooms() {
        rooms.add(new Room("101A", "Single", 100.00));
        rooms.add(new Room("101B", "Single", 100.00));
        rooms.add(new Room("102A", "Single", 100.00));
        rooms.add(new Room("102B", "Single", 100.00));
        rooms.add(new Room("201A", "Double", 150.00));
        rooms.add(new Room("201B", "Double", 150.00));
        rooms.add(new Room("202A", "Double", 150.00));
        rooms.add(new Room("202B", "Double", 150.00));
        rooms.add(new Room("301A", "Suite", 250.00));
        rooms.add(new Room("301B", "Suite", 250.00));
        rooms.add(new Room("301C", "Suite", 250.00));
        rooms.add(new Room("301D", "Suite", 250.00));
        saveToFile();
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

    public List<Room> getAvailableRoomsForDates(LocalDate checkIn, LocalDate checkOut, ReservationManager reservationManager) {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable() && isRoomAvailableForDates(room.getRoomNumber(), checkIn, checkOut, reservationManager)) {
                available.add(room);
            }
        }
        return available;
    }

    public int getAvailableCountByType(String roomType, LocalDate checkIn, LocalDate checkOut, ReservationManager reservationManager) {
        int total = 0;
        for (Room room : rooms) {
            if (room.getRoomType().equals(roomType) && room.isAvailable() && isRoomAvailableForDates(room.getRoomNumber(), checkIn, checkOut, reservationManager)) {
                total++;
            }
        }
        return total;
    }

    public int getTotalCountByType(String roomType) {
        int count = 0;
        for (Room room : rooms) {
            if (room.getRoomType().equals(roomType)) {
                count++;
            }
        }
        return count;
    }

    public Map<String, Integer> getAvailabilityByType(LocalDate checkIn, LocalDate checkOut, ReservationManager reservationManager) {
        Map<String, Integer> availability = new HashMap<>();
        for (Room room : rooms) {
            String type = room.getRoomType();
            if (!availability.containsKey(type)) {
                availability.put(type, getAvailableCountByType(type, checkIn, checkOut, reservationManager));
            }
        }
        return availability;
    }

    private boolean isRoomAvailableForDates(String roomNumber, LocalDate checkIn, LocalDate checkOut, ReservationManager reservationManager) {
        List<Reservation> reservations = reservationManager.getReservationsByRoom(roomNumber);
        for (Reservation res : reservations) {
            if (res.getStatus().equals("Active")) {
                if (!(checkOut.isBefore(res.getCheckInDate()) || checkIn.isAfter(res.getCheckOutDate()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public Room findRoomByNumber(String roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    public boolean isRoomAvailable(String roomNumber) {
        Room room = findRoomByNumber(roomNumber);
        return room != null && room.isAvailable();
    }

    public void addRoom(Room room) {
        rooms.add(room);
        saveToFile();
    }

    public boolean removeRoom(String roomNumber) {
        Room room = findRoomByNumber(roomNumber);
        if (room != null) {
            rooms.remove(room);
            saveToFile();
            return true;
        }
        return false;
    }

    public void updateRoom(Room updatedRoom) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber().equals(updatedRoom.getRoomNumber())) {
                rooms.set(i, updatedRoom);
                break;
            }
        }
        saveToFile();
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Room room : rooms) {
                writer.println(room.getRoomNumber() + "|" + room.getRoomType() + "|" + 
                    room.getPricePerNight() + "|" + room.isAvailable());
            }
        } catch (IOException e) {
            System.out.println("Error saving rooms: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    String roomNumber = parts[0];
                    String roomType = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    boolean available = Boolean.parseBoolean(parts[3]);
                    Room room = new Room(roomNumber, roomType, price);
                    room.setAvailable(available);
                    rooms.add(room);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading rooms: " + e.getMessage());
        }
    }
}