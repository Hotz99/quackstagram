import javax.swing.SwingUtilities;

import auth.SignInUI;
import auth.userManager;

public class Main {
    private static userManager userManager = new userManager();
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignInUI frame = new SignInUI(userManager);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
