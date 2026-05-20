import javax.swing.*;
import java.awt.*;

public class SignUpPage extends JFrame {

    private final Color PRIMARY   = new Color(59, 130, 246);
    private final Color BG_WHITE  = Color.WHITE;
    private final Color TEXT_DARK = new Color(15, 23, 42);
    private final Color TEXT_LIGHT = new Color(100, 116, 139);
    private final Color SUCCESS   = new Color(34, 197, 94);
    private final Color DANGER    = new Color(239, 68, 68);
    private UserManager userManager = new UserManager();

    public SignUpPage() {
        setTitle("UTHM Guest House - Sign Up");
        setSize(420, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Title
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Fill in the details below");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        JTextField nameField      = createField();
        JTextField usernameField  = createField();
        JTextField emailField     = createField();
        JPasswordField passField  = createPasswordField();
        JPasswordField confirmField = createPasswordField();

        // Error / success label
        JLabel messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sign up button
        JButton signUpBtn = new JButton("Create Account");
        signUpBtn.setBackground(SUCCESS);
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signUpBtn.setBorderPainted(false);
        signUpBtn.setFocusPainted(false);
        signUpBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        signUpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        signUpBtn.addActionListener(e -> {
            String name     = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String email    = emailField.getText().trim();
            String pass     = new String(passField.getPassword());
            String confirm  = new String(confirmField.getPassword());

            // Validation
            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                messageLabel.setForeground(DANGER);
                messageLabel.setText("All fields are required.");
                return;
            }
            if (!email.contains("@")) {
                messageLabel.setForeground(DANGER);
                messageLabel.setText("Please enter a valid email.");
                return;
            }
            if (pass.length() < 6) {
                messageLabel.setForeground(DANGER);
                messageLabel.setText("Password must be at least 6 characters.");
                return;
            }
            if (!pass.equals(confirm)) {
                messageLabel.setForeground(DANGER);
                messageLabel.setText("Passwords do not match.");
                return;
            }

            boolean saved = userManager.saveUser(username, pass, email, name);
            // Success (save user then go to login)
            if (saved) {
            messageLabel.setForeground(new Color(34, 197, 94));
            messageLabel.setText("Account created! Redirecting to login...");

            Timer timer = new Timer(1500, ev -> {
                dispose();
                new LoginPage().setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            // saveUser returns false if username already exists
            messageLabel.setForeground(DANGER);
            messageLabel.setText("Username already taken. Please choose another.");
        }
        });

        // Back to login link
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        backPanel.setBackground(BG_WHITE);
        backPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel backText = new JLabel("Already have an account? ");
        backText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backText.setForeground(TEXT_DARK);

        JLabel backLink = new JLabel("<html><u>Log in</u></html>");
        backLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backLink.setForeground(PRIMARY);
        backLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backLink.setForeground(new Color(29, 78, 216));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                backLink.setForeground(PRIMARY);
            }
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new LoginPage().setVisible(true);
            }
        });

        backPanel.add(backText);
        backPanel.add(backLink);

        // Assemble layout
        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(25));
        addField(panel, "Full Name",        nameField);
        addField(panel, "Username",         usernameField);
        addField(panel, "Email",            emailField);
        addField(panel, "Password",         passField);
        addField(panel, "Confirm Password", confirmField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(messageLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(signUpBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(backPanel);

        add(panel);
    }

    // Helper: label + field pair
    private void addField(JPanel panel, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
        panel.add(field);
        panel.add(Box.createVerticalStrut(12));
    }

  private JTextField createField() {
    JTextField field = new JTextField();
    field.setAlignmentX(Component.CENTER_ALIGNMENT);
    field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    field.setPreferredSize(new Dimension(300, 38));      // ← add this
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(226, 232, 240)),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    return field;
}


    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
}