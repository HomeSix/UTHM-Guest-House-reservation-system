public class GuestHouse {
    private String roomId;
    private String roomType;  // "Single", "Double", "Suite"
    // need to change this based on UTHM's punya structures.
    private double pricePerNight;
    private int capacity;  // Number of guests the room can accommodate
    private boolean isAvailable;

    // a lot of these needs changing
    public GuestHouse(String roomId, String roomType, double pricePerNight, int capacity) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.isAvailable = true;  // Rooms are available by default
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // Check if available
    public String getAvailabilityStatus() {
        return isAvailable ? "Available" : "Not Available";
    }


    // Display information
    @Override
    public String toString() {
        return "GuestHouse{" +
                "roomId='" + roomId + '\'' +
                ", roomType='" + roomType + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", capacity=" + capacity +
                ", status='" + getAvailabilityStatus() + '\'' +
                '}';
    }
}
