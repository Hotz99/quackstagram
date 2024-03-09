package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import auth.SignInUI;
import auth.UserManager;

public class App extends JFrame {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 500;

    public static UserManager userManager = new UserManager();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignInUI frame = new SignInUI();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
