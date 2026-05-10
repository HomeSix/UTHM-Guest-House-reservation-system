import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Class to manage all reservation-related operations, including CRUD and file persistence

public class ReservationManager {
    private ArrayList<Reservation> reservations;
    private final String DATA_FILE = "reservations.txt";

    // Constructor - Initialize the reservation manager and load existing reservations from file
    public ReservationManager() {
        this.reservations = new ArrayList<>();
        loadFromFile();
    }

    // CREATE OPERATION
    public boolean insertReservation(Reservation reservation) {
        if (reservation == null) {
            System.out.println("Error: Cannot add null reservation.");
            return false;
        }

        // Check if reservation ID already exists
        if (findReservationById(reservation.getReservationId()) != null) {
            System.out.println("Error: Reservation ID already exists.");
            return false;
        }

        // Check if room is available for the requested dates
        if (!isRoomAvailableForDates(reservation.getRoom().getId(), 
                                     reservation.getCheckInDate(), 
                                     reservation.getCheckOutDate())) {
            System.out.println("Error: Room is not available for the requested dates.");
            return false;
        }

        reservations.add(reservation);
        saveToFile();  // Persist to file after insertion
        System.out.println("✓ Reservation added successfully!");
        return true;
    }

    // READ OPERATIONS
    public Reservation findReservationById(String reservationId) {
        for (Reservation res : reservations) {
            if (res.getReservationId().equals(reservationId)) {
                return res;
            }
        }
        return null;
    }

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    // Get reservations for a specific guest
    public List<Reservation> getReservationsByGuest(String guestId) {
        ArrayList<Reservation> result = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getGuest().getGuestId().equals(guestId)) {
                result.add(res);
            }
        }
        return result;
    }

    // Get reservations by guest name (partial match, case-insensitive)
    public List<Reservation> getReservationsByGuestName(String guestName) {
        ArrayList<Reservation> result = new ArrayList<>();
        String searchName = guestName.toLowerCase();
        for (Reservation res : reservations) {
            if (res.getGuest().getName().toLowerCase().contains(searchName)) {
                result.add(res);
            }
        }
        return result;
    }

    // Get reservations for a specific room
    public List<Reservation> getReservationsByRoom(String roomId) {
        ArrayList<Reservation> result = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getRoom().getId().equals(roomId)) {
                result.add(res);
            }
        }
        return result;
    }

    // Get reservations by status (e.g., Active, Completed, Cancelled)
    public List<Reservation> getReservationsByStatus(String status) {
        ArrayList<Reservation> result = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getStatus().equals(status)) {
                result.add(res);
            }
        }
        return result;
    }

    // UPDATE OPERATION
    public boolean removeReservation(String reservationId) {
        Reservation toRemove = findReservationById(reservationId);
        if (toRemove != null) {
            reservations.remove(toRemove);
            saveToFile();  // Persist to file after removal
            System.out.println("✓ Reservation removed successfully!");
            return true;
        }
        System.out.println("Error: Reservation not found.");
        return false;
    }

    // Remove all reservations with a specific status (e.g., Cancelled)
    public int removeReservationsByStatus(String status) {
        int count = 0;
        ArrayList<Reservation> toRemove = new ArrayList<>();
        
        for (Reservation res : reservations) {
            if (res.getStatus().equals(status)) {
                toRemove.add(res);
                count++;
            }
        }

        reservations.removeAll(toRemove);
        if (count > 0) {
            saveToFile();
        }
        return count;
    }

    // Check if a room is available for the given dates (no overlapping active reservations)
    private boolean isRoomAvailableForDates(String roomId, 
                                           java.time.LocalDate checkInDate, 
                                           java.time.LocalDate checkOutDate) {
        for (Reservation res : reservations) {
            if (res.getRoom().getId().equals(roomId) && 
                res.getStatus().equals("Active")) {
                // Check for date overlap
                if (!checkOutDate.isBefore(res.getCheckInDate()) && 
                    !checkInDate.isAfter(res.getCheckOutDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    // Get total number of reservations
    public int getTotalReservations() {
        return reservations.size();
    }

    // Display all reservations in a formatted manner
    public void displayAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        System.out.println("\n========== ALL RESERVATIONS ==========");
        for (int i = 0; i < reservations.size(); i++) {
            System.out.println((i + 1) + ". " + reservations.get(i));
        }
        System.out.println("=====================================\n");
    }

    //  FILE PERSISTENCE METHODS 
    /**
     * Save all reservations to a text file
     * This method serializes reservation data to persistent storage
     */
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Reservation res : reservations) {
                // Format: ResID|GuestID|GuestName|RoomID|CheckIn|CheckOut|Status|TotalCost
                writer.println(
                    res.getReservationId() + "|" +
                    res.getGuest().getGuestId() + "|" +
                    res.getGuest().getName() + "|" +
                    res.getRoom().getId() + "|" +
                    res.getCheckInDate() + "|" +
                    res.getCheckOutDate() + "|" +
                    res.getStatus() + "|" +
                    res.getTotalCost()
                );
            }
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    // Load reservations from file at startup
    private void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No previous data found. Starting with empty reservations.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 8) {
                    // Reconstruct reservation from file data
                    String resId = parts[0];
                    String guestId = parts[1];
                    String guestName = parts[2];
                    String roomId = parts[3];
                    java.time.LocalDate checkIn = java.time.LocalDate.parse(parts[4]);
                    java.time.LocalDate checkOut = java.time.LocalDate.parse(parts[5]);
                    String status = parts[6];

                    String formattedGuestId = guestId.startsWith("G") ? guestId : "G" + guestId;
                    Guest guest = new Guest(formattedGuestId, guestName, "email@example.com", "0000000000");
                    Room room = new Room(roomId, "Standard", 100.0, 2);

                    Reservation res = new Reservation(resId, guest, room, checkIn, checkOut);
                    res.setStatus(status);
                    reservations.add(res);
                }
            }
            System.out.println("Data loaded from file. Total reservations: " + reservations.size());
        } catch (IOException e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
    }
}
