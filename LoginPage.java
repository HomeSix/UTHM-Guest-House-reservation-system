// LoginPage.java
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LoginPage extends JFrame {
    private Image logoImage;
    private final Color PRIMARY   = new Color(59, 130, 246);
    private final Color BG_WHITE  = Color.WHITE;
    private final Color TEXT_DARK = new Color(15, 23, 42);
    private final Color DANGER    = new Color(239, 68, 68);

    private UserManager userManager = new UserManager();

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }



    public LoginPage() {
        setTitle("UTHM Guest House - Login");
        setSize(420, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Load logo
        JLabel logoLabel;
        try {
            ImageIcon logoIcon = new ImageIcon("resources/uthm_logo.png");
            Image scaled = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            logoLabel = new JLabel("UTHM"); // fallback text if image missing
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            logoLabel.setForeground(PRIMARY);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Title
        JLabel title = new JLabel("UTHM Guest House");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Please login to continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(TEXT_DARK);
         userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Password field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passLabel.setForeground(TEXT_DARK);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Error label (hidden until needed)
        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(DANGER);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(PRIMARY);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Replace your single JLabel with a panel containing two labels
        JPanel signUpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        signUpPanel.setBackground(BG_WHITE);
        signUpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel signUpText = new JLabel("Don't have an account? ");
        signUpText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        signUpText.setForeground(TEXT_DARK);

        JLabel signUpLink = new JLabel("<html><u>Sign up</u></html>"); // underline via HTML
        signUpLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        signUpLink.setForeground(PRIMARY);                             // blue like a real link
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));          // pointer cursor on hover

        // Hover effect
        signUpLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                signUpLink.setForeground(new Color(29, 78, 216)); // darker blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                signUpLink.setForeground(PRIMARY);
            }
            public void mouseClicked(java.awt.event.MouseEvent e) {
                LoadingScreen.run(LoginPage.this, () -> new SignUpPage().setVisible(true));
            }
        });

signUpPanel.add(signUpText);
signUpPanel.add(signUpLink);

        // Login action — same for button click or pressing Enter
        Runnable doLogin = () -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Please enter username and password.");
                return;
            }
            if (userManager.verifyLogin(user, pass)) {
                String fullName = userManager.getFullName(user);
                LoadingScreen.run(LoginPage.this, () -> new GuestHouseGUI(fullName).setVisible(true));
            } else {
                errorLabel.setText("Invalid username or password.");
                passwordField.setText("");
            }
        };

        loginBtn.addActionListener(e -> doLogin.run());
        passwordField.addActionListener(e -> doLogin.run()); // Enter key works too

        // Assemble layout
        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(28));
        panel.add(userLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(passLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(errorLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loginBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(signUpPanel);
        add(panel);
    }
}