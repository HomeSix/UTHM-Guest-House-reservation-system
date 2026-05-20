import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestHouseGUI extends JFrame {
    private ReservationManager reservationManager;
    private RoomManager roomManager;
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    private Image logoImage;
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String loggedInUser;
    
    private final Color PRIMARY = new Color(59, 130, 246);
    private final Color SUCCESS = new Color(34, 197, 94);
    private final Color DANGER = new Color(239, 68, 68);
    private final Color TEXT_DARK = new Color(15, 23, 42);
    private final Color TEXT_LIGHT = new Color(100, 116, 139);
    private final Color BG_LIGHT = new Color(248, 250, 252);
    private final Color BG_WHITE = Color.WHITE;
    private final Color BORDER = new Color(226, 232, 240);

    public static void main(String[] args) {
       // SwingUtilities.invokeLater(() -> new GuestHouseGUI().setVisible(true));
    }

      public GuestHouseGUI(String fullName) {
        this.loggedInUser = fullName;
        this.reservationManager = new ReservationManager();
        this.roomManager = new RoomManager();
        loadLogo();
        initComponents();
        loadReservations();
    }
    //For Dev Testing 
     public GuestHouseGUI() {
        // this.reservationManager = new ReservationManager();
        // this.roomManager = new RoomManager();
        // loadLogo();
        // initComponents();
        // loadReservations();
        this("Developer");
    }

    private void loadLogo() {
        try {
            logoImage = ImageIO.read(new File("resources/uthm_logo.png"));
        } catch (IOException e) {
            logoImage = null;
        }
    }

    private void initComponents() {
        setTitle("UTHM Guest House Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_LIGHT);

        setupSidebar();
        setupMainContent();
    }

    private void setupSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_WHITE);
        sidebar.setPreferredSize(new Dimension(240, 800));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));

        if (logoImage != null) {
            Image scaledLogo = logoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
            sidebar.add(logoLabel);
        } else {
            JLabel logoPlaceholder = new JLabel("LOGO");
            logoPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            logoPlaceholder.setForeground(TEXT_LIGHT);
            logoPlaceholder.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
            sidebar.add(logoPlaceholder);
        }

        JLabel title = new JLabel("UTHM Guest House");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        welcomeLabel.setForeground(TEXT_LIGHT);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(welcomeLabel); // add after the title label
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(BG_WHITE);

        String[] menuItems = {"Dashboard", "Add Reservation", "View All", "Manage Rooms"};
        String[] icons = {"◉", "+", "☰", "↻", "◈"};

        for (int i = 0; i < menuItems.length; i++) {
            final int index = i;
            JButton btn = createMenuButton(icons[i] + "  " + menuItems[i]);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(e -> handleMenuAction(menuItems[index]));
            menuPanel.add(btn);
            menuPanel.add(Box.createVerticalStrut(5));
        }

        JPanel spacer = new JPanel();
        spacer.setLayout(new BoxLayout(spacer, BoxLayout.Y_AXIS));
        spacer.setBackground(BG_WHITE);
        spacer.add(Box.createVerticalGlue());

        // JButton exitBtn = createMenuButton("✕  Exit");
        // exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        // exitBtn.addActionListener(e -> System.exit(0));

        // In setupSidebar(), before the exitBtn section
JButton logoutBtn = createMenuButton("⎋  Logout");
logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
logoutBtn.addActionListener(e -> {
    int confirm = JOptionPane.showConfirmDialog(
        this, "Are you sure you want to logout?",
        "Logout", JOptionPane.YES_NO_OPTION
    );
    if (confirm == JOptionPane.YES_OPTION) {
        LoadingScreen.run(GuestHouseGUI.this, () -> new LoginPage().setVisible(true));
    }
});

sidebar.add(logoutBtn);
// sidebar.add(exitBtn); // already exists
        sidebar.add(title);
        sidebar.add(menuPanel);
        sidebar.add(spacer);
        // sidebar.add(exitBtn);
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(TEXT_DARK);
        btn.setBackground(BG_WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(200, 42));
        btn.setPreferredSize(new Dimension(200, 42));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(239, 246, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG_WHITE);
            }
        });
        
        return btn;
    }

    private void setupMainContent() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(BG_LIGHT);
        mainContent.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel headerLabel = new JLabel("Dashboard");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(TEXT_DARK);

        JPanel statsPanel = createStatsPanel();

        mainContent.add(headerLabel, BorderLayout.NORTH);
        mainContent.add(statsPanel, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createStatsPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_LIGHT);

        JPanel reservationsSection = new JPanel();
        reservationsSection.setLayout(new BoxLayout(reservationsSection, BoxLayout.Y_AXIS));
        reservationsSection.setBackground(BG_LIGHT);
        
        JLabel reservationHeader = new JLabel("RESERVATIONS");
        reservationHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        reservationHeader.setForeground(TEXT_LIGHT);
        reservationHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel resRow = new JPanel(new GridLayout(1, 4, 8, 8));
        resRow.setBackground(BG_LIGHT);

        int total = reservationManager.getTotalReservations();
        int active = reservationManager.getReservationsByStatus("Active").size();
        int completed = reservationManager.getReservationsByStatus("Completed").size();
        int rejected = reservationManager.getReservationsByStatus("Rejected").size();

        resRow.add(createStatCard("Total", String.valueOf(total), PRIMARY, Color.WHITE));
        resRow.add(createStatCard("Active", String.valueOf(active), SUCCESS, Color.WHITE));
        resRow.add(createStatCard("Completed", String.valueOf(completed), new Color(100, 116, 139), Color.WHITE));
        resRow.add(createStatCard("Rejected", String.valueOf(rejected), DANGER, Color.WHITE));

        reservationsSection.add(reservationHeader);
        reservationsSection.add(resRow);
        
        JPanel roomsSection = new JPanel();
        roomsSection.setLayout(new BoxLayout(roomsSection, BoxLayout.Y_AXIS));
        roomsSection.setBackground(BG_LIGHT);
        roomsSection.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        
        JLabel roomsHeader = new JLabel("AVAILABLE ROOMS");
        roomsHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roomsHeader.setForeground(TEXT_LIGHT);
        roomsHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel roomsRow = new JPanel(new GridLayout(1, 3, 8, 8));
        roomsRow.setBackground(BG_LIGHT);

        int availableSingle = roomManager.getAvailableCountByType("Single", LocalDate.now(), LocalDate.now().plusDays(1), reservationManager);
        int availableDouble = roomManager.getAvailableCountByType("Double", LocalDate.now(), LocalDate.now().plusDays(1), reservationManager);
        int availableSuite = roomManager.getAvailableCountByType("Suite", LocalDate.now(), LocalDate.now().plusDays(1), reservationManager);

        roomsRow.add(createStatCard("Single", String.valueOf(availableSingle), new Color(139, 92, 246), Color.WHITE));
        roomsRow.add(createStatCard("Double", String.valueOf(availableDouble), new Color(249, 115, 22), Color.WHITE));
        roomsRow.add(createStatCard("Suite", String.valueOf(availableSuite), new Color(16, 185, 129), Color.WHITE));

        roomsSection.add(roomsHeader);
        roomsSection.add(roomsRow);

        mainPanel.add(reservationsSection);
        mainPanel.add(roomsSection);

        return mainPanel;
    }

    private JPanel createStatCard(String title, String value, Color bgColor, Color textColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(bgColor);
        card.setPreferredSize(new Dimension(280, 120));
        card.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(textColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(255, 255, 255, 180));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private void handleMenuAction(String action) {
        switch (action) {
            case "Dashboard":
                showDashboard();
                break;
            case "Add Reservation":
                showAddReservationDialog();
                break;
            case "View All":
                showAllReservations();
                break;
            case "Manage Rooms":
                showManageRoomsDialog();
                break;
        }
    }

    private void showDashboard() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_LIGHT);
        setupSidebar();

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(BG_LIGHT);
        mainContent.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel headerLabel = new JLabel("Dashboard");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(TEXT_DARK);

        mainContent.add(headerLabel, BorderLayout.NORTH);
        mainContent.add(createStatsPanel(), BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showAddReservationDialog() {
        JDialog dialog = new JDialog(this, "Add New Reservation", true);
        dialog.setSize(520, 580);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_WHITE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        container.setBackground(BG_WHITE);

        JLabel title = new JLabel("Add New Reservation");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel guestSection = createSectionLabel("Guest Information");
        JLabel reservationSection = createSectionLabel("Reservation Details");

        JPanel guestInfo = new JPanel(new GridLayout(4, 2, 10, 10));
        guestInfo.setBackground(BG_WHITE);
        guestInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        String nextGuestId = generateNextGuestId();
        JTextField guestIdField = createTextField(nextGuestId);
        guestIdField.setEditable(false);
        guestIdField.setBackground(new Color(245, 245, 245));
        JTextField guestNameField = createTextField("");
        JTextField emailField = createTextField("");
        JTextField phoneField = createTextField("");

        guestInfo.add(createLabel("Guest ID:"));
        guestInfo.add(guestIdField);
        guestInfo.add(createLabel("Guest Name:"));
        guestInfo.add(guestNameField);
        guestInfo.add(createLabel("Email:"));
        guestInfo.add(emailField);
        guestInfo.add(createLabel("Phone:"));
        guestInfo.add(phoneField);

        List<Room> availableRooms = roomManager.getAvailableRooms();
        JComboBox<Room> roomCombo = new JComboBox<>();
        roomCombo.addItem(null);
        for (Room room : availableRooms) {
            roomCombo.addItem(room);
        }
        roomCombo.setSelectedItem(null);
        roomCombo.setBackground(BG_WHITE);
        roomCombo.setForeground(TEXT_DARK);
        roomCombo.setPreferredSize(new Dimension(200, 35));
        roomCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("SELECT A ROOM");
                } else if (value instanceof Room) {
                    Room room = (Room) value;
                    setText(room.toDisplayString());
                }
                return this;
            }
        });

        JLabel availabilityLabel = new JLabel();
        availabilityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        availabilityLabel.setForeground(TEXT_LIGHT);
        availabilityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField priceField = createTextField("0.00");
        priceField.setEditable(false);
        priceField.setBackground(new Color(245, 245, 245));
        
        LocalDate today = LocalDate.now();
        java.util.Date tomorrow = java.sql.Date.valueOf(today.plusDays(1));
        java.util.Date dayAfter = java.sql.Date.valueOf(today.plusDays(2));
        
        SpinnerDateModel checkInModel = new SpinnerDateModel(tomorrow, null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner checkInSpinner = new JSpinner(checkInModel);
        checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd"));
        checkInSpinner.setPreferredSize(new Dimension(200, 35));
        
        SpinnerDateModel checkOutModel = new SpinnerDateModel(dayAfter, null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner checkOutSpinner = new JSpinner(checkOutModel);
        checkOutSpinner.setEditor(new JSpinner.DateEditor(checkOutSpinner, "yyyy-MM-dd"));
        checkOutSpinner.setPreferredSize(new Dimension(200, 35));

        JTextField totalCostField = createTextField("0.00");
        totalCostField.setEditable(false);
        totalCostField.setBackground(new Color(245, 245, 245));

        roomCombo.addActionListener(e -> {
            Room selected = (Room) roomCombo.getSelectedItem();
            if (selected != null) {
                priceField.setText(String.format("%.2f", selected.getPricePerNight()));
                updateTotalCostFromSpinner(checkInSpinner, checkOutSpinner, selected.getPricePerNight(), totalCostField);
            } else {
                priceField.setText("0.00");
                totalCostField.setText("0.00");
            }
        });

        javax.swing.event.ChangeListener costListener = e -> {
            Room selected = (Room) roomCombo.getSelectedItem();
            if (selected != null) {
                updateTotalCostFromSpinner(checkInSpinner, checkOutSpinner, selected.getPricePerNight(), totalCostField);
            }
            updateRoomAvailability(roomCombo, availabilityLabel, checkInSpinner, checkOutSpinner);
        };
        checkInSpinner.addChangeListener(costListener);
        checkOutSpinner.addChangeListener(costListener);

        updateRoomAvailability(roomCombo, availabilityLabel, checkInSpinner, checkOutSpinner);

        JPanel resInfo = new JPanel(new GridLayout(6, 2, 10, 10));
        resInfo.setBackground(BG_WHITE);

        resInfo.add(createLabel("Select Room:"));
        resInfo.add(roomCombo);
        resInfo.add(createLabel(""));
        resInfo.add(availabilityLabel);
        resInfo.add(createLabel("Price/Night (RM):"));
        resInfo.add(priceField);
        resInfo.add(createLabel("Check-in Date:"));
        resInfo.add(checkInSpinner);
        resInfo.add(createLabel("Check-out Date:"));
        resInfo.add(checkOutSpinner);
        resInfo.add(createLabel("Total Cost (RM):"));
        resInfo.add(totalCostField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttons.setBackground(BG_WHITE);

        JButton saveBtn = createActionButton("Save Reservation", SUCCESS);
        JButton cancelBtn = createActionButton("Cancel", new Color(100, 116, 139));

        saveBtn.setPreferredSize(new Dimension(150, 40));
        cancelBtn.setPreferredSize(new Dimension(100, 40));

        saveBtn.addActionListener(e -> {
            try {
                String gId = guestIdField.getText();
                String gName = guestNameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                Room selectedRoom = (Room) roomCombo.getSelectedItem();

                if (gName.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter guest name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter email", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (phone.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter phone number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedRoom == null) {
                    JOptionPane.showMessageDialog(dialog, "Please select a room from the dropdown", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate checkIn = ((java.util.Date) checkInSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                LocalDate checkOut = ((java.util.Date) checkOutSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                if (checkIn.isBefore(today)) {
                    JOptionPane.showMessageDialog(dialog, "Check-in date cannot be in the past", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                    JOptionPane.showMessageDialog(dialog, "Check-out date must be after check-in date", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Guest guest = new Guest(gId, gName, email, phone);
                String resId = generateReservationId();
                Reservation reservation = new Reservation(resId, guest, selectedRoom, checkIn, checkOut);

                if (reservationManager.insertReservation(reservation)) {
                    dialog.toFront();
                    JOptionPane.showMessageDialog(dialog, "Reservation added successfully!\nTotal Cost: RM" + String.format("%.2f", reservation.getTotalCost()), "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    dialog.toFront();
                    JOptionPane.showMessageDialog(dialog, "Failed to add reservation. Room may not be available.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttons.add(cancelBtn);
        buttons.add(saveBtn);

        container.add(title);
        container.add(guestSection);
        container.add(guestInfo);
        container.add(reservationSection);
        container.add(resInfo);
        container.add(buttons);

        dialog.add(container);
        dialog.setVisible(true);
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(PRIMARY);
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));
        return label;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_DARK);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setBackground(BG_WHITE);
        field.setForeground(TEXT_DARK);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setCaretColor(TEXT_DARK);
        field.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        field.setPreferredSize(new Dimension(200, 35));
        field.setMargin(new Insets(8, 10, 8, 10));
        return field;
    }

    private void updateTotalCost(JTextField checkInField, JTextField checkOutField, double pricePerNight, JTextField totalCostField) {
        try {
            LocalDate checkIn = LocalDate.parse(checkInField.getText(), DATE_FORMATTER);
            LocalDate checkOut = LocalDate.parse(checkOutField.getText(), DATE_FORMATTER);
            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (nights > 0) {
                double total = nights * pricePerNight;
                totalCostField.setText(String.format("%.2f", total));
            }
        } catch (Exception e) {
            totalCostField.setText("0.00");
        }
    }

    private void updateTotalCostFromSpinner(JSpinner checkInSpinner, JSpinner checkOutSpinner, double pricePerNight, JTextField totalCostField) {
        try {
            LocalDate checkIn = ((java.util.Date) checkInSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = ((java.util.Date) checkOutSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (nights > 0) {
                double total = nights * pricePerNight;
                totalCostField.setText(String.format("%.2f", total));
            } else {
                totalCostField.setText("0.00");
            }
        } catch (Exception e) {
            totalCostField.setText("0.00");
        }
    }

    private void updateRoomAvailability(JComboBox<Room> roomCombo, JLabel availabilityLabel, JSpinner checkInSpinner, JSpinner checkOutSpinner) {
        try {
            LocalDate checkIn = ((java.util.Date) checkInSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = ((java.util.Date) checkOutSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            
            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                availabilityLabel.setText("Invalid dates selected");
                return;
            }

            List<Room> availableRooms = roomManager.getAvailableRoomsForDates(checkIn, checkOut, reservationManager);
            Map<String, Integer> availability = new java.util.HashMap<>();
            
            for (Room room : availableRooms) {
                String type = room.getRoomType();
                availability.put(type, availability.getOrDefault(type, 0) + 1);
            }

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Integer> entry : availability.entrySet()) {
                int total = roomManager.getTotalCountByType(entry.getKey());
                int available = entry.getValue();
                if (available == 0) {
                    sb.append(entry.getKey() + ": FULL, ");
                } else {
                    sb.append(entry.getKey() + ": " + available + "/" + total + " left, ");
                }
            }
            
            String text = sb.toString();
            if (!text.isEmpty()) {
                text = text.substring(0, text.length() - 2);
            }
            availabilityLabel.setText(text);
            
            Object selected = roomCombo.getSelectedItem();
            roomCombo.removeAllItems();
            roomCombo.addItem(null);
            for (Room room : availableRooms) {
                roomCombo.addItem(room);
            }
            roomCombo.setSelectedItem(selected);

        } catch (Exception e) {
            availabilityLabel.setText("");
        }
    }

    private void showAllReservations() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_LIGHT);
        setupSidebar();

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(BG_LIGHT);
        mainContent.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_LIGHT);

        JLabel headerLabel = new JLabel("All Reservations");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(TEXT_DARK);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(BG_LIGHT);

        JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{"All", "By Reservation ID", "By Guest Name", "By Room Number", "By Status"});
        searchTypeCombo.setBackground(BG_WHITE);
        searchTypeCombo.setForeground(TEXT_DARK);
        searchTypeCombo.setPreferredSize(new Dimension(150, 35));

        JTextField searchField = createTextField("Search...");
        searchField.setPreferredSize(new Dimension(180, 35));

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Completed", "Cancelled", "Rejected"});
        statusCombo.setBackground(BG_WHITE);
        statusCombo.setForeground(TEXT_DARK);
        statusCombo.setPreferredSize(new Dimension(180, 35));
        statusCombo.setVisible(false);

        JPanel inputPanel = new JPanel(new CardLayout());
        inputPanel.setBackground(BG_LIGHT);
        inputPanel.add(searchField, "text");
        inputPanel.add(statusCombo, "status");

        searchTypeCombo.addActionListener(e -> {
            boolean isStatus = "By Status".equals(searchTypeCombo.getSelectedItem());
            searchField.setVisible(!isStatus);
            statusCombo.setVisible(isStatus);
            inputPanel.revalidate();
            inputPanel.repaint();
        });

        searchField.addActionListener(e -> performSearch(searchTypeCombo, searchField, statusCombo));
        statusCombo.addActionListener(e -> performSearch(searchTypeCombo, searchField, statusCombo));

        JButton searchBtn = createActionButton("Search", PRIMARY);
        searchBtn.setPreferredSize(new Dimension(90, 35));
        searchBtn.addActionListener(e -> performSearch(searchTypeCombo, searchField, statusCombo));

        JButton clearBtn = createActionButton("Clear", new Color(100, 116, 139));
        clearBtn.setPreferredSize(new Dimension(90, 35));
        clearBtn.addActionListener(e -> {
            searchTypeCombo.setSelectedIndex(0);
            searchField.setText("");
            searchField.setVisible(true);
            statusCombo.setVisible(false);
            inputPanel.revalidate();
            loadReservations();
        });

        searchPanel.add(searchTypeCombo);
        searchPanel.add(inputPanel);
        searchPanel.add(clearBtn);
        searchPanel.add(searchBtn);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BG_LIGHT);

        JButton refreshBtn = createActionButton("Refresh", PRIMARY);
        refreshBtn.addActionListener(e -> {
            searchTypeCombo.setSelectedIndex(0);
            searchField.setText("");
            searchField.setVisible(true);
            statusCombo.setVisible(false);
            inputPanel.revalidate();
            loadReservations();
        });

        buttonPanel.add(refreshBtn);
        
        JButton actionsBtn = createActionButton("Actions ▼", new Color(100, 116, 139));
        actionsBtn.setPreferredSize(new Dimension(110, 35));

        JPopupMenu actionsMenu = new JPopupMenu();

        JMenu updateSub = new JMenu("Update Status");
        for (String s : new String[]{"Active", "Completed", "Cancelled", "Rejected"}) {
            JMenuItem item = new JMenuItem(s);
            String status = s;
            item.addActionListener(e -> updateSelectedStatus(status));
            updateSub.add(item);
        }
        actionsMenu.add(updateSub);
        actionsMenu.addSeparator();

        JMenuItem deleteItem = new JMenuItem("Delete Selected");
        deleteItem.addActionListener(e -> deleteSelectedReservations());
        actionsMenu.add(deleteItem);

        JMenuItem exportItem = new JMenuItem("Export Excel");
        exportItem.addActionListener(e -> exportToExcel());
        actionsMenu.add(exportItem);

        actionsBtn.addActionListener(e -> actionsMenu.show(actionsBtn, 0, actionsBtn.getHeight()));

        buttonPanel.add(actionsBtn);

        header.add(headerLabel, BorderLayout.NORTH);
        header.add(searchPanel, BorderLayout.CENTER);
        header.add(buttonPanel, BorderLayout.EAST);

        String[] columns = {"", "Reservation ID", "Guest Name", "Room No.", "Check-in", "Check-out", "Nights", "Total (RM)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }
        };

        reservationTable = new JTable(tableModel);
        reservationTable.setBackground(BG_WHITE);
        reservationTable.setForeground(TEXT_DARK);
        reservationTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reservationTable.setRowHeight(40);
        reservationTable.getTableHeader().setBackground(PRIMARY);
        reservationTable.getTableHeader().setForeground(Color.WHITE);
        reservationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        reservationTable.setGridColor(BORDER);
        reservationTable.setSelectionBackground(new Color(219, 234, 254));
        reservationTable.setSelectionForeground(TEXT_DARK);
        reservationTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        reservationTable.getColumnModel().getColumn(0).setMaxWidth(40);

        JCheckBox headerCheckBox = new JCheckBox();
        headerCheckBox.setBackground(PRIMARY);
        headerCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        headerCheckBox.setBorderPainted(false);
        headerCheckBox.setFocusPainted(false);

        reservationTable.getColumnModel().getColumn(0).setHeaderRenderer(new javax.swing.table.TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                boolean allChecked = table.getRowCount() > 0;
                for (int i = 0; i < table.getRowCount(); i++) {
                    Boolean v = (Boolean) table.getValueAt(i, 0);
                    if (v == null || !v) { allChecked = false; break; }
                }
                headerCheckBox.setSelected(allChecked);
                return headerCheckBox;
            }
        });

        reservationTable.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = reservationTable.columnAtPoint(e.getPoint());
                if (col == 0) {
                    boolean allChecked = tableModel.getRowCount() > 0;
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        Boolean v = (Boolean) tableModel.getValueAt(i, 0);
                        if (v == null || !v) { allChecked = false; break; }
                    }
                    boolean newVal = !allChecked;
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        tableModel.setValueAt(newVal, i, 0);
                    }
                    reservationTable.getTableHeader().resizeAndRepaint();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(reservationTable);
        scrollPane.getViewport().setBackground(BG_WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        mainContent.add(header, BorderLayout.NORTH);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
        loadReservations();
        revalidate();
        repaint();
    }

    private void deleteSelectedReservations() {
        List<String> toDelete = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean selected = (Boolean) tableModel.getValueAt(i, 0);
            if (selected != null && selected) {
                String resId = (String) tableModel.getValueAt(i, 1);
                toDelete.add(resId);
            }
        }

        if (toDelete.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one reservation to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete " + toDelete.size() + " reservation(s)?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            for (String resId : toDelete) {
                reservationManager.removeReservation(resId);
            }
            loadReservations();
            JOptionPane.showMessageDialog(this, toDelete.size() + " reservation(s) deleted successfully!");
        }
    }

    private void updateSelectedStatus(String newStatus) {
        List<String> toUpdate = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean selected = (Boolean) tableModel.getValueAt(i, 0);
            if (selected != null && selected) {
                String resId = (String) tableModel.getValueAt(i, 1);
                toUpdate.add(resId);
            }
        }

        if (toUpdate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one reservation.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Update " + toUpdate.size() + " reservation(s) to \"" + newStatus + "\"?",
            "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            for (String resId : toUpdate) {
                reservationManager.updateReservationStatus(resId, newStatus);
            }
            loadReservations();
            JOptionPane.showMessageDialog(this, toUpdate.size() + " reservation(s) updated to \"" + newStatus + "\"!");
        }
    }

    private void exportToExcel() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Excel File");
            fileChooser.setSelectedFile(new java.io.File("reservations_export.xls"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.PrintWriter writer = new java.io.PrintWriter(file);
                
                writer.print("Reservation ID\tGuest Name\tRoom No.\tCheck-in\tCheck-out\tNights\tTotal (RM)\tStatus\n");
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 1; j < tableModel.getColumnCount(); j++) {
                        writer.print(tableModel.getValueAt(i, j) + "\t");
                    }
                    writer.println();
                }
                
                writer.close();
                JOptionPane.showMessageDialog(this, "Exported to Excel successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error exporting: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch(JComboBox<String> searchTypeCombo, JTextField searchField, JComboBox<String> statusCombo) {
        String type = (String) searchTypeCombo.getSelectedItem();
        String query = searchField.getText().trim().toLowerCase();

        tableModel.setRowCount(0);

        if (type.equals("All")) {
            List<Reservation> all = reservationManager.getAllReservations();
            for (Reservation res : all) {
                if (query.isEmpty() ||
                    res.getReservationId().toLowerCase().contains(query) ||
                    res.getGuest().getName().toLowerCase().contains(query) ||
                    res.getRoom().getRoomNumber().toLowerCase().contains(query) ||
                    res.getStatus().toLowerCase().contains(query)) {
                    addReservationRow(res);
                }
            }
            if (reservationTable != null) {
                reservationTable.getTableHeader().resizeAndRepaint();
            }
            return;
        }

        if (query.isEmpty() && !type.equals("By Status")) return;

        List<Reservation> results = new ArrayList<>();

        switch (type) {
            case "By Reservation ID":
                for (Reservation r : reservationManager.getAllReservations()) {
                    if (r.getReservationId().toLowerCase().contains(query)) {
                        results.add(r);
                    }
                }
                break;
            case "By Guest Name":
                results = reservationManager.getReservationsByGuestName(query);
                break;
            case "By Room Number":
                for (Reservation r : reservationManager.getAllReservations()) {
                    if (r.getRoom().getRoomNumber().toLowerCase().contains(query)) {
                        results.add(r);
                    }
                }
                break;
            case "By Status":
                String status = (String) statusCombo.getSelectedItem();
                results = reservationManager.getReservationsByStatus(status);
                break;
        }

        for (Reservation res : results) {
            addReservationRow(res);
        }
        if (reservationTable != null) {
            reservationTable.getTableHeader().resizeAndRepaint();
        }
    }

    private void addReservationRow(Reservation res) {
        String roomNumber = res.getRoom().getRoomNumber();
        Room actualRoom = roomManager.findRoomByNumber(roomNumber);
        String displayRoom = actualRoom != null ? actualRoom.getRoomNumber() : roomNumber;

        Object[] row = {
            false,
            res.getReservationId(),
            res.getGuest().getName(),
            displayRoom,
            res.getCheckInDate(),
            res.getCheckOutDate(),
            res.getNumberOfNights(),
            String.format("%.2f", res.getTotalCost()),
            res.getStatus()
        };
        tableModel.addRow(row);
    }

    private void loadReservations() {
        if (tableModel != null) {
            tableModel.setRowCount(0);
            for (Reservation res : reservationManager.getAllReservations()) {
                addReservationRow(res);
            }
            if (reservationTable != null) {
                reservationTable.getTableHeader().resizeAndRepaint();
            }
        }
    }

    private void deleteSelectedReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow >= 0) {
            String resId = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete reservation " + resId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                reservationManager.removeReservation(resId);
                loadReservations();
                JOptionPane.showMessageDialog(this, "Reservation deleted successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a reservation to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showSearchDialog() {
        JDialog dialog = new JDialog(this, "Search Reservations", true);
        dialog.setSize(480, 400);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_WHITE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        container.setBackground(BG_WHITE);

        JLabel title = new JLabel("Search Reservations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(BG_WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JComboBox<String> searchType = new JComboBox<>(new String[]{"By Reservation ID", "By Guest ID", "By Guest Name", "By Room ID", "By Status"});
        searchType.setBackground(BG_WHITE);
        searchType.setForeground(TEXT_DARK);
        searchType.setMaximumSize(new Dimension(350, 38));

        JTextField searchField = createTextField("");
        searchField.setMaximumSize(new Dimension(350, 38));

        JLabel resultLabel = new JLabel("");
        resultLabel.setForeground(TEXT_DARK);
        resultLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.setBackground(BG_WHITE);

        JButton searchBtn = createActionButton("Search", PRIMARY);
        JButton closeBtn = createActionButton("Close", TEXT_LIGHT);

        searchBtn.addActionListener(e -> {
            String type = (String) searchType.getSelectedItem();
            String query = searchField.getText().trim();

            if (query.isEmpty()) {
                resultLabel.setText("Please enter a search value.");
                return;
            }

            switch (type) {
                case "By Reservation ID":
                    Reservation r = reservationManager.findReservationById(query);
                    if (r != null) {
                        String roomNum = r.getRoom().getRoomNumber();
                        Room actualRoom = roomManager.findRoomByNumber(roomNum);
                        String displayRoom = actualRoom != null ? actualRoom.getRoomNumber() : roomNum;
                        resultLabel.setText("<html>Found: " + r.getReservationId() + "<br>" + r.getGuest().getName() + " | " + displayRoom + " | " + r.getStatus() + "</html>");
                    } else {
                        resultLabel.setText("No reservation found.");
                    }
                    break;
                case "By Guest ID":
                    List<Reservation> results = reservationManager.getReservationsByGuest(query);
                    resultLabel.setText("Found " + results.size() + " reservation(s)");
                    break;
                case "By Guest Name":
                    List<Reservation> resultsByName = reservationManager.getReservationsByGuestName(query);
                    if (resultsByName.isEmpty()) {
                        resultLabel.setText("No reservations found.");
                    } else {
                        resultLabel.setText("Found " + resultsByName.size() + " reservation(s)");
                    }
                    break;
                case "By Room ID":
                    results = reservationManager.getReservationsByRoom(query);
                    resultLabel.setText("Found " + results.size() + " reservation(s)");
                    break;
                case "By Status":
                    results = reservationManager.getReservationsByStatus(query);
                    resultLabel.setText("Found " + results.size() + " reservation(s)");
                    break;
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        buttons.add(closeBtn);
        buttons.add(searchBtn);

        form.add(searchType);
        form.add(Box.createVerticalStrut(15));
        form.add(searchField);
        form.add(Box.createVerticalStrut(15));
        form.add(resultLabel);

        container.add(title);
        container.add(form);
        container.add(buttons);

        dialog.add(container);
        dialog.setVisible(true);
    }

    private void showUpdateStatusDialog() {
        JDialog dialog = new JDialog(this, "Update Reservation Status", true);
        dialog.setSize(480, 350);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_WHITE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        container.setBackground(BG_WHITE);

        JLabel title = new JLabel("Update Reservation Status");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(BG_WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel resIdLabel = new JLabel("Select Reservation:");
        resIdLabel.setForeground(TEXT_DARK);
        resIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        List<Reservation> allReservations = reservationManager.getAllReservations();
        String[] reservationOptions = new String[allReservations.size()];
        for (int i = 0; i < allReservations.size(); i++) {
            Reservation r = allReservations.get(i);
            reservationOptions[i] = r.getReservationId() + " - " + r.getGuest().getName();
        }
        
        JComboBox<String> resIdCombo = new JComboBox<>(reservationOptions);
        resIdCombo.setBackground(BG_WHITE);
        resIdCombo.setForeground(TEXT_DARK);
        resIdCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resIdCombo.setMaximumSize(new Dimension(350, 38));

        JLabel statusLabel = new JLabel("New Status:");
        statusLabel.setForeground(TEXT_DARK);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Completed", "Cancelled", "Rejected"});
        statusCombo.setBackground(BG_WHITE);
        statusCombo.setForeground(TEXT_DARK);
        statusCombo.setMaximumSize(new Dimension(350, 38));

        JLabel resultLabel = new JLabel("");
        resultLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.setBackground(BG_WHITE);

        JButton updateBtn = createActionButton("Update Status", SUCCESS);
        JButton closeBtn = createActionButton("Close", TEXT_LIGHT);

        updateBtn.addActionListener(e -> {
            String selected = (String) resIdCombo.getSelectedItem();
            if (selected == null) {
                resultLabel.setForeground(DANGER);
                resultLabel.setText("No reservation selected.");
                return;
            }
            String resId = selected.split(" - ")[0];
            String newStatus = (String) statusCombo.getSelectedItem();

            Reservation res = reservationManager.findReservationById(resId);
            if (res != null) {
                reservationManager.updateReservationStatus(resId, newStatus);
                resultLabel.setForeground(SUCCESS);
                resultLabel.setText("Status updated to: " + newStatus);
            } else {
                resultLabel.setForeground(DANGER);
                resultLabel.setText("Reservation not found.");
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        buttons.add(closeBtn);
        buttons.add(updateBtn);

        form.add(resIdLabel);
        form.add(resIdCombo);
        form.add(Box.createVerticalStrut(15));
        form.add(statusLabel);
        form.add(statusCombo);
        form.add(Box.createVerticalStrut(15));
        form.add(resultLabel);

        container.add(title);
        container.add(form);
        container.add(buttons);

        dialog.add(container);
        dialog.setVisible(true);
    }

    private void showManageRoomsDialog() {
        JDialog dialog = new JDialog(this, "Manage Rooms", true);
        dialog.setSize(750, 600);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_WHITE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.setBackground(BG_WHITE);

        JLabel title = new JLabel("Room Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] columns = {"Room Number", "Room Type", "Price/Night (RM)", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable roomTable = new JTable(tableModel);
        roomTable.setBackground(BG_WHITE);
        roomTable.setForeground(TEXT_DARK);
        roomTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roomTable.setRowHeight(35);
        roomTable.getTableHeader().setBackground(PRIMARY);
        roomTable.getTableHeader().setForeground(Color.WHITE);
        roomTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        roomTable.setGridColor(BORDER);
        roomTable.setSelectionBackground(new Color(219, 234, 254));

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        scrollPane.setPreferredSize(new Dimension(650, 250));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.setBackground(BG_WHITE);

        JButton addBtn = createActionButton("Add Room", SUCCESS);
        JButton editBtn = createActionButton("Edit Room", PRIMARY);
        JButton deleteBtn = createActionButton("Delete Room", DANGER);
        JButton refreshBtn = createActionButton("Refresh", TEXT_LIGHT);
        JButton closeBtn = createActionButton("Close", new Color(100, 116, 139));

        loadRoomTable(tableModel);

        addBtn.addActionListener(e -> showAddRoomDialog(dialog, tableModel));
        editBtn.addActionListener(e -> {
            int selected = roomTable.getSelectedRow();
            if (selected >= 0) {
                String roomNumber = (String) tableModel.getValueAt(selected, 0);
                showEditRoomDialog(dialog, roomNumber, tableModel);
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a room to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        deleteBtn.addActionListener(e -> {
            int selected = roomTable.getSelectedRow();
            if (selected >= 0) {
                String roomNumber = (String) tableModel.getValueAt(selected, 0);
                int confirm = JOptionPane.showConfirmDialog(dialog, "Delete room " + roomNumber + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    roomManager.removeRoom(roomNumber);
                    loadRoomTable(tableModel);
                    JOptionPane.showMessageDialog(dialog, "Room deleted successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a room to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        refreshBtn.addActionListener(e -> loadRoomTable(tableModel));
        closeBtn.addActionListener(e -> dialog.dispose());

        buttons.add(refreshBtn);
        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);
        buttons.add(closeBtn);

        container.add(title);
        container.add(Box.createVerticalStrut(15));
        container.add(scrollPane);
        container.add(Box.createVerticalStrut(15));
        container.add(buttons);

        dialog.add(container);
        dialog.setVisible(true);
    }

    private void loadRoomTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Room room : roomManager.getAllRooms()) {
            Object[] row = {
                room.getRoomNumber(),
                room.getRoomType(),
                String.format("%.2f", room.getPricePerNight()),
                room.getAvailabilityStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddRoomDialog(JDialog parent, DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(parent, "Add New Room", true);
        dialog.setSize(450, 420);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(BG_WHITE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        container.setBackground(BG_WHITE);

        JLabel title = new JLabel("Add New Room");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBackground(BG_WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JTextField roomNumberField = createTextField("");
        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        roomTypeCombo.setBackground(BG_WHITE);
        roomTypeCombo.setForeground(TEXT_DARK);
        JTextField priceField = createTextField("100.00");
        JCheckBox availableCheck = new JCheckBox("Available", true);
        availableCheck.setBackground(BG_WHITE);
        availableCheck.setForeground(TEXT_DARK);

        addFormRow(form, "Room Number:", roomNumberField);
        addFormRow(form, "Room Type:", roomTypeCombo);
        addFormRow(form, "Price (RM):", priceField);
        addFormRow(form, "Status:", availableCheck);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttons.setBackground(BG_WHITE);

        JButton saveBtn = createActionButton("Save", SUCCESS);
        JButton cancelBtn = createActionButton("Cancel", TEXT_LIGHT);

        saveBtn.addActionListener(e -> {
            try {
                String roomNumber = roomNumberField.getText().trim();
                String roomType = (String) roomTypeCombo.getSelectedItem();
                double price = Double.parseDouble(priceField.getText());
                boolean available = availableCheck.isSelected();

                if (roomNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter room number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (roomManager.findRoomByNumber(roomNumber) != null) {
                    JOptionPane.showMessageDialog(dialog, "Room number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Room newRoom = new Room(roomNumber, roomType, price);
                newRoom.setAvailable(available);
                roomManager.addRoom(newRoom);
                loadRoomTable(tableModel);
                JOptionPane.showMessageDialog(dialog, "Room added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttons.add(cancelBtn);
        buttons.add(saveBtn);

        container.add(title);
        container.add(form);
        container.add(buttons);

        dialog.add(container);
        dialog.setVisible(true);
    }

    private void showEditRoomDialog(JDialog parent, String roomNumber, DefaultTableModel tableModel) {
        Room room = roomManager.findRoomByNumber(roomNumber);
        if (room == null) return;

        JDialog dialog = new JDialog(parent, "Edit Room", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(BG_WHITE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        container.setBackground(BG_WHITE);

        JLabel title = new JLabel("Edit Room");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBackground(BG_WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JTextField roomNumberField = createTextField(room.getRoomNumber());
        roomNumberField.setEditable(false);
        roomNumberField.setBackground(new Color(240, 240, 240));

        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        roomTypeCombo.setBackground(BG_WHITE);
        roomTypeCombo.setForeground(TEXT_DARK);
        roomTypeCombo.setSelectedItem(room.getRoomType());

        JTextField priceField = createTextField(String.valueOf(room.getPricePerNight()));
        JCheckBox availableCheck = new JCheckBox("Available", room.isAvailable());
        availableCheck.setBackground(BG_WHITE);
        availableCheck.setForeground(TEXT_DARK);

        addFormRow(form, "Room Number:", roomNumberField);
        addFormRow(form, "Room Type:", roomTypeCombo);
        addFormRow(form, "Price (RM):", priceField);
        addFormRow(form, "Status:", availableCheck);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttons.setBackground(BG_WHITE);

        JButton saveBtn = createActionButton("Update", SUCCESS);
        JButton cancelBtn = createActionButton("Cancel", TEXT_LIGHT);

        saveBtn.addActionListener(e -> {
            try {
                String roomType = (String) roomTypeCombo.getSelectedItem();
                double price = Double.parseDouble(priceField.getText());
                boolean available = availableCheck.isSelected();

                room.setRoomType(roomType);
                room.setPricePerNight(price);
                room.setAvailable(available);
                
                roomManager.updateRoom(room);
                loadRoomTable(tableModel);
                JOptionPane.showMessageDialog(dialog, "Room updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttons.add(cancelBtn);
        buttons.add(saveBtn);

        container.add(title);
        container.add(form);
        container.add(buttons);

        dialog.add(container);
        dialog.setVisible(true);
    }

    private void addFormRow(JPanel panel, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT_DARK);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setPreferredSize(new Dimension(130, 38));
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lbl);
        panel.add(field);
    }

    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private String generateReservationId() {
        LocalDate today = LocalDate.now();
        int random = (int) (Math.random() * 9000) + 1000;
        return "RES-" + today.toString().replace("-", "") + "-" + random;
    }

    private String generateNextGuestId() {
        int maxId = 0;
        List<Reservation> reservations = reservationManager.getAllReservations();
        for (Reservation res : reservations) {
            String guestId = res.getGuest().getGuestId();
            if (guestId != null && guestId.startsWith("G")) {
                try {
                    int num = Integer.parseInt(guestId.substring(1));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
        return "G" + String.format("%03d", maxId + 1);
    }
}