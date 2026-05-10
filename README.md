# UTHM Guest House Reservation System
## Group 6 - Software Development Project

### Project Overview
This is a Java-based Guest House Reservation System for UTHM's group project assignment. The system allows users to:
- Add new reservations
- Remove existing reservations
- View all reservations or search by specific criteria
- Update reservation status
- Persist data to a text file

---

## Project Structure

### Classes Overview

#### 1. **Entity.java** (Base Class)
- Abstract base class demonstrating **inheritance**
- Parent class for all system entities
- Provides common attributes: `id`, `createdTime`
- All subclasses must implement `toString()`

#### 2. **Guest.java**
- Represents a guest in the system
- **Attributes**: guestId, name, email, phoneNumber
- **Methods**: Getters (Accessors), Setters (Mutators), toString()

#### 3. **GuestHouse.java**
- Represents a guest house room/unit
- **Attributes**: roomId, roomType, pricePerNight, capacity, isAvailable
- **Methods**: Getters, Setters, status checking methods

#### 4. **Reservation.java** (Extends Entity)
- Represents a reservation
- **Attributes**: reservationId, guest, guestHouse, checkInDate, checkOutDate, status, totalCost
- **Methods**: 
  - Getters and Setters
  - `calculateTotalCost()` - Calculates cost based on number of nights
  - `getNumberOfNights()` - Returns duration of stay
- Demonstrates **inheritance** by extending Entity

#### 5. **ReservationManager.java** (Manager/Service Class)
- Handles all business logic and CRUD operations
- **Features**:
  - **Create**: `insertReservation()` - Add new reservations
  - **Read**: `findReservationById()`, `getAllReservations()`, `getReservationsByGuest()`, `getReservationsByRoom()`, `getReservationsByStatus()`
  - **Update**: Reservations can be modified via accessors
  - **Delete**: `removeReservation()`, `removeReservationsByStatus()`
- **Data Persistence**:
  - `saveToFile()` - Saves all reservations to `reservations.txt`
  - `loadFromFile()` - Loads reservations from file on startup
- Uses **ArrayList** as data structure

#### 6. **GuestReservationSystem.java** (Main Class)
- Entry point of the application
- Provides interactive menu-driven interface
- Handles user input and display
- Manages application flow

---

## OOP Elements Implemented

✅ **Classes**: Entity, Guest, GuestHouse, Reservation, ReservationManager, GuestReservationSystem

✅ **Attributes**: Each class has multiple private attributes

✅ **Constructors**: All classes have constructors to initialize objects

✅ **Accessors (Getters)**: All classes implement getter methods

✅ **Mutators (Setters)**: All classes implement setter methods

✅ **Methods**: Business logic methods like `calculateTotalCost()`, `insertReservation()`, etc.

✅ **Inheritance**: Reservation extends Entity (abstract base class)

✅ **Data Structure**: Uses **ArrayList** to store reservations with file-based persistence

✅ **Detailed Comments**: All methods and classes include comprehensive comments

---

## How to Run

### Compilation
```bash
javac *.java
```

### Execution
```bash
java GuestReservationSystem FOR TERMINAL
java GuestHouseGUI FOR GUI
```

### First Run
On first run, the system will create an empty reservation system. Follow the menu prompts to add reservations.

### Subsequent Runs
The system automatically loads previously saved reservations from `reservations.txt`

---

## File Persistence

### Data File: `reservations.txt`
Format (pipe-delimited):
```
ResID|GuestID|GuestName|RoomID|CheckIn|CheckOut|Status|TotalCost
```

Example:
```
RES-20260507-1234|G001|John Doe|R101|2026-05-10|2026-05-15|Active|500.0
```

---

## Features to Improve / Extend

You can enhance this base system with:

1. **Database Integration** - Replace text file with SQL database (MySQL, SQLite)
2. **User Authentication** - Add login system for staff/admin
3. **GUI** - Convert CLI to graphical interface (Swing, JavaFX)
4. **Payment Processing** - Add payment module and billing system
5. **Advanced Searching** - Filter by date range, price range, room type
6. **Reporting** - Generate revenue reports, occupancy statistics
7. **Notifications** - Email/SMS reminders for check-in/check-out
8. **Room Management** - Add, edit, remove rooms; manage room inventory
9. **Guest Profiles** - Store guest history, preferences, loyalty points
10. **Validation** - Add comprehensive input validation and error handling

---

## Menu Options

1. **Add New Reservation** - Create a new reservation with guest and room details
2. **View All Reservations** - Display all reservations in the system
3. **Search Reservation** - Search by Reservation ID, Guest ID, Room ID, or Status
4. **Remove Reservation** - Delete a reservation by ID
5. **Update Reservation Status** - Change reservation status (Active/Completed/Cancelled)
6. **Exit** - Close the application

---

## Sample Test Data

Try creating a reservation with:
- Guest ID: G001
- Guest Name: Ahmad Abdullah
- Email: ahmad@example.com
- Phone: 0123456789
- Room ID: R101
- Room Type: Double
- Price: 150.00 (RM per night)
- Capacity: 2
- Check-in: 2026-05-10
- Check-out: 2026-05-15

---

## Notes

- All dates must be in format: **yyyy-MM-dd**
- Reservation IDs are auto-generated
- Total cost is calculated automatically based on number of nights
- Room availability is checked before booking to prevent double-booking
- All data is automatically saved after each operation

---

## Group 6
UTHM - Faculty of Computer Science and Information Technology
Course: BIK10503 - Software Development
April 2026
