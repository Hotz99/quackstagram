import javax.swing.SwingUtilities;

import auth.SignInUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignInUI frame = new SignInUI();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
