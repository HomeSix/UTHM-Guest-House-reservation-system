import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation extends Entity {
    private String reservationId;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;  // "Active", "Completed", "Cancelled"
    private double totalCost;

    public Reservation(String reservationId, Guest guest, Room room, 
                      LocalDate checkInDate, LocalDate checkOutDate) {
        super(reservationId);  // Call parent constructor with ID
        this.reservationId = reservationId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = "Active";
        this.totalCost = calculateTotalCost();
    }

    public String getReservationId() {
        return reservationId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        this.totalCost = calculateTotalCost();  // Recalculate cost when date changes
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        this.totalCost = calculateTotalCost();  // Recalculate cost when date changes
    }

    // calculate total cost based on number of nights and price per night
    private double calculateTotalCost() {
        if (checkInDate != null && checkOutDate != null && room != null) {
            long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            return numberOfNights * room.getPricePerNight();
        }
        return 0.0;
    }

    // Calculate the number of nights for the reservation
    public long getNumberOfNights() {
        if (checkInDate != null && checkOutDate != null) {
            return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
        return 0;
    }

    // Display reservation information
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", guest=" + guest.getName() +
                ", room=" + room.getRoomNumber() +
                ", checkIn=" + checkInDate +
                ", checkOut=" + checkOutDate +
                ", nights=" + getNumberOfNights() +
                ", totalCost=RM" + String.format("%.2f", totalCost) +
                ", status='" + status + '\'' +
                '}';
    }
}
