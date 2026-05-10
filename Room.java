public class Room extends Entity {
    private String roomType;
    private double pricePerNight;
    private int capacity;
    private boolean isAvailable;

    public Room(String roomId, String roomType, double pricePerNight, int capacity) {
        super(roomId);
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.isAvailable = true;
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
        this.isAvailable = available;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAvailabilityStatus() {
        return isAvailable ? "Available" : "Not Available";
    }

    @Override
    public String toString() {
        return roomType + " - RM" + String.format("%.2f", pricePerNight) + "/night";
    }

    public String toDisplayString() {
        return getId() + " (" + roomType + ")";
    }
}