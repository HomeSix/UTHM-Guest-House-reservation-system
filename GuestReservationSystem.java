import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class GuestReservationSystem {
    private ReservationManager reservationManager;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public GuestReservationSystem() {
        this.reservationManager = new ReservationManager();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        GuestReservationSystem system = new GuestReservationSystem();
        system.run();
    }

    public void run() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   UTHM Guest House Reservation System          ║");
        System.out.println("║   Group 6 - Software Development Project       ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = getUserInput("Enter your choice (1-6): ");

            switch (choice) {
                case "1":
                    addNewReservation(); // Add New Reservation function
                    break;
                case "2":
                    viewAllReservations(); // View All Reservations function
                    break;
                case "3":
                    searchReservation(); // Search Reservation function
                    break;
                case "4":
                    removeReservation(); // Remove Reservation function
                    break;
                case "5":
                    updateReservationStatus(); // Update Reservation Status function
                    break;
                case "6":
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
        System.out.println("6. Exit");
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
