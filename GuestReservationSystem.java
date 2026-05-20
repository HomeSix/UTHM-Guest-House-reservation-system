import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class GuestReservationSystem {
    private ReservationManager reservationManager;
    private RoomManager roomManager;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public GuestReservationSystem() {
        this.reservationManager = new ReservationManager();
        this.roomManager = new RoomManager();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        GuestReservationSystem system = new GuestReservationSystem();
        system.run();
    }

    public void run() {
        showLoading();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   UTHM Guest House Reservation System          ║");
        System.out.println("║   Group 6 - Software Development Project       ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = getUserInput("Enter your choice (1-7): ");

            if (!choice.equals("7")) showLoading();
            switch (choice) {
                case "1":
                    addNewReservation();
                    break;
                case "2":
                    viewAllReservations();
                    break;
                case "3":
                    searchReservation();
                    break;
                case "4":
                    removeReservation();
                    break;
                case "5":
                    updateReservationStatus();
                    break;
                case "6":
                    manageRooms();
                    break;
                case "7":
                    running = false;
                    System.out.println("\nThank you for using UTHM Guest House Reservation System!");
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.\n");
            }
        }
        scanner.close();
    }

    // Display main menu
    private void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Add New Reservation");
        System.out.println("2. View All Reservations");
        System.out.println("3. Search Reservation");
        System.out.println("4. Remove Reservation");
        System.out.println("5. Update Reservation Status");
        System.out.println("6. Manage Rooms");
        System.out.println("7. Exit");
        System.out.println("===============================");
    }

    // Add a new reservation to the system
    private void addNewReservation() {
        System.out.println("\n========== ADD NEW RESERVATION ==========");

        // Collect guest information
        String guestId = getUserInput("Enter Guest ID: ");
        String guestName = getUserInput("Enter Guest Name: ");
        String email = getUserInput("Enter Guest Email: ");
        String phone = getUserInput("Enter Guest Phone: ");

        Guest guest = new Guest(guestId, guestName, email, phone);

        // Collect room information
        String roomId = getUserInput("Enter Room ID: ");
        String roomType = getUserInput("Enter Room Type (Single/Double/Suite): ");
        String roomNumber = getUserInput("Enter Room Number (e.g., 101A): ");
        double price = Double.parseDouble(getUserInput("Enter Price Per Night (RM): "));

        Room room = new Room(roomNumber, roomType, price);

        // Collect reservation dates
        String checkInStr = getUserInput("Enter Check-in Date (yyyy-MM-dd): ");
        String checkOutStr = getUserInput("Enter Check-out Date (yyyy-MM-dd): ");

        try {
            LocalDate checkIn = LocalDate.parse(checkInStr, DATE_FORMATTER);
            LocalDate checkOut = LocalDate.parse(checkOutStr, DATE_FORMATTER);

            // Validate dates
            if (checkOut.isBefore(checkIn)) {
                System.out.println("❌ Error: Check-out date cannot be before check-in date.");
                return;
            }

            // Create and add reservation
            String reservationId = generateReservationId();
            Reservation reservation = new Reservation(reservationId, guest, room, checkIn, checkOut);

            if (reservationManager.insertReservation(reservation)) {
                System.out.println("Reservation Details:");
                System.out.println(reservation);
            }
        } catch (Exception e) {
            System.out.println("❌ Error: Invalid date format or input. Please try again.");
        }
    }

    // Display all reservations in the system
    private void viewAllReservations() {
        System.out.println("\n========== VIEW ALL RESERVATIONS ==========");
        reservationManager.displayAllReservations();
    }

    // Search for a reservation
    private void searchReservation() {
        System.out.println("\n========== SEARCH RESERVATION ==========");
        System.out.println("1. Search by Reservation ID");
        System.out.println("2. Search by Guest ID");
        System.out.println("3. Search by Room ID");
        System.out.println("4. Search by Status");

        String choice = getUserInput("Enter your choice (1-4): ");

        switch (choice) {
            case "1":
                searchByReservationId();
                break;
            case "2":
                searchByGuestId();
                break;
            case "3":
                searchByRoomId();
                break;
            case "4":
                searchByStatus();
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }

    //
    // Search reservation by ID
    private void searchByReservationId() {
        String resId = getUserInput("Enter Reservation ID: ");
        Reservation res = reservationManager.findReservationById(resId);

        if (res != null) {
            System.out.println("\n✓ Found: " + res);
        } else {
            System.out.println("❌ Reservation not found.");
        }
    }

    // Search reservations by guest ID
    private void searchByGuestId() {
        String guestId = getUserInput("Enter Guest ID: ");
        List<Reservation> results = reservationManager.getReservationsByGuest(guestId);

        if (results.isEmpty()) {
            System.out.println("❌ No reservations found for this guest.");
        } else {
            System.out.println("\n✓ Found " + results.size() + " reservation(s):");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }

    // Search reservations by room ID
    private void searchByRoomId() {
        String roomId = getUserInput("Enter Room ID: ");
        List<Reservation> results = reservationManager.getReservationsByRoom(roomId);

        if (results.isEmpty()) {
            System.out.println("❌ No reservations found for this room.");
        } else {
            System.out.println("\n✓ Found " + results.size() + " reservation(s):");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }

    // Search reservations by status
    private void searchByStatus() {
        System.out.println("Available statuses: Active, Completed, Cancelled");
        String status = getUserInput("Enter Status: ");
        List<Reservation> results = reservationManager.getReservationsByStatus(status);

        if (results.isEmpty()) {
            System.out.println("❌ No reservations found with this status.");
        } else {
            System.out.println("\n✓ Found " + results.size() + " reservation(s):");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }

    // Remove a reservation from the system
    private void removeReservation() {
        System.out.println("\n========== REMOVE RESERVATION ==========");
        String resId = getUserInput("Enter Reservation ID to remove: ");

        if (reservationManager.removeReservation(resId)) {
            System.out.println("Reservation removed successfully.");
        }
    }

    // Update reservation status
    private void updateReservationStatus() {
        System.out.println("\n========== UPDATE RESERVATION STATUS ==========");
        String resId = getUserInput("Enter Reservation ID: ");
        Reservation res = reservationManager.findReservationById(resId);

        if (res != null) {
            System.out.println("Current Status: " + res.getStatus());
            System.out.println("Available statuses: Active, Completed, Cancelled");
            String newStatus = getUserInput("Enter new status: ");
            res.setStatus(newStatus);
            System.out.println("✓ Status updated successfully!");
        } else {
            System.out.println("❌ Reservation not found.");
        }
    }

    private void manageRooms() {
        boolean running = true;
        while (running) {
            System.out.println("\n========== MANAGE ROOMS ==========");
            System.out.println("1. View All Rooms");
            System.out.println("2. Add New Room");
            System.out.println("3. Edit Room");
            System.out.println("4. Delete Room");
            System.out.println("5. Back to Main Menu");
            System.out.println("===================================");
            String choice = getUserInput("Enter your choice (1-5): ");

            switch (choice) {
                case "1":
                    viewAllRooms();
                    break;
                case "2":
                    addNewRoom();
                    break;
                case "3":
                    editRoom();
                    break;
                case "4":
                    deleteRoom();
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllRooms() {
        System.out.println("\n========== ALL ROOMS ==========");
        System.out.printf("%-15s %-12s %-15s %-15s%n", "Room Number", "Room Type", "Price/Night (RM)", "Status");
        System.out.println("------------------------------------------------------------");
        
        List<Room> rooms = roomManager.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for (Room room : rooms) {
                System.out.printf("%-15s %-12s %-15.2f %-15s%n",
                    room.getRoomNumber(),
                    room.getRoomType(),
                    room.getPricePerNight(),
                    room.getAvailabilityStatus()
                );
            }
        }
        System.out.println("------------------------------------------------------------");
        System.out.println("Total Rooms: " + rooms.size());
    }

    private void addNewRoom() {
        System.out.println("\n========== ADD NEW ROOM ==========");
        
        String roomNumber = getUserInput("Enter Room Number: ");
        if (roomNumber.isEmpty()) {
            System.out.println("❌ Room number cannot be empty.");
            return;
        }
        if (roomManager.findRoomByNumber(roomNumber) != null) {
            System.out.println("❌ Room already exists!");
            return;
        }

        System.out.println("Room types: Single, Double, Suite");
        String roomType = getUserInput("Enter Room Type: ");
        
        double price;
        try {
            price = Double.parseDouble(getUserInput("Enter Price Per Night (RM): "));
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid price format.");
            return;
        }

        Room room = new Room(roomNumber, roomType, price);
        roomManager.addRoom(room);
        System.out.println("✓ Room added successfully!");
    }

    private void editRoom() {
        System.out.println("\n========== EDIT ROOM ==========");
        String roomNumber = getUserInput("Enter Room Number to edit: ");
        if (roomNumber.isEmpty()) {
            System.out.println("❌ Room number cannot be empty.");
            return;
        }
        
        Room room = roomManager.findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("❌ Room not found.");
            return;
        }

        System.out.println("Current details:");
        System.out.printf("  Room Type: %s%n", room.getRoomType());
        System.out.printf("  Price/Night: RM%.2f%n", room.getPricePerNight());
        System.out.printf("  Status: %s%n", room.getAvailabilityStatus());

        String newType = getUserInput("Enter new Room Type (press Enter to keep current): ");
        if (!newType.isEmpty()) {
            room.setRoomType(newType);
        }

        String priceStr = getUserInput("Enter new Price (press Enter to keep current): ");
        if (!priceStr.isEmpty()) {
            try {
                room.setPricePerNight(Double.parseDouble(priceStr));
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid price format.");
                return;
            }
        }

        String availStr = getUserInput("Enter availability (Available/Not Available, press Enter to keep current): ");
        if (!availStr.isEmpty()) {
            room.setAvailable(availStr.equalsIgnoreCase("Available"));
        }

        roomManager.updateRoom(room);
        System.out.println("✓ Room updated successfully!");
    }

    private void deleteRoom() {
        System.out.println("\n========== DELETE ROOM ==========");
        String roomNumber = getUserInput("Enter Room Number to delete: ");
        if (roomNumber.isEmpty()) {
            System.out.println("❌ Room number cannot be empty.");
            return;
        }
        
        if (roomManager.removeRoom(roomNumber)) {
            System.out.println("✓ Room deleted successfully!");
        } else {
            System.out.println("❌ Room not found.");
        }
    }

    private void showLoading() {
        try {
            System.out.print("Loading");
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                Thread.sleep(150);
            }
            System.out.print("\r");
            for (int i = 0; i < 10; i++) System.out.print(" ");
            System.out.print("\r");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // get user input
    private String getUserInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Generate a unique reservation ID
     * Format: RES-YYYYMMDD-NNNN (where NNNN is a random number)
     * @return Generated reservation ID
     */
    private String generateReservationId() {
        java.time.LocalDate today = java.time.LocalDate.now();
        int random = (int) (Math.random() * 9000) + 1000;
        return "RES-" + today.toString().replace("-", "") + "-" + random;
    }
}
