import javax.swing.*;
import java.awt.*;

public class LoadingScreen {
    private JWindow window;
    private JProgressBar progressBar;

    private final Color PRIMARY = new Color(59, 130, 246);
    private final Color TEXT_LIGHT = new Color(100, 116, 139);

    public LoadingScreen() {
        window = new JWindow();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));

        JLabel icon = new JLabel("◉");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        icon.setForeground(PRIMARY);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msg = new JLabel("Please wait");
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        msg.setForeground(TEXT_LIGHT);
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(180, 4));
        progressBar.setForeground(PRIMARY);
        progressBar.setBackground(new Color(219, 234, 254));
        progressBar.setBorderPainted(false);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(icon);
        panel.add(Box.createVerticalStrut(15));
        panel.add(msg);
        panel.add(Box.createVerticalStrut(15));
        panel.add(progressBar);

        window.add(panel);
        window.pack();
        window.setLocationRelativeTo(null);
    }

    public void show() {
        window.setVisible(true);
    }

    public void hide() {
        window.setVisible(false);
        window.dispose();
    }

    public static void run(JFrame current, Runnable openNext) {
        current.dispose();
        LoadingScreen ls = new LoadingScreen();
        ls.show();
        Timer timer = new Timer(700, e -> {
            ls.hide();
            openNext.run();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
