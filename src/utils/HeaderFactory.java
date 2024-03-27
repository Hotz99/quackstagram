package utils;

import app.App;
import java.awt.*;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HeaderFactory {

  private static final int HEADER_HEIGHT = 40; // Corrected static size for bottom icons

  public JPanel createHeaderPanel(String label) {
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
    JLabel lblRegister = new JLabel(label);
    lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
    lblRegister.setForeground(Color.WHITE); // Set the text color to white
    headerPanel.add(lblRegister);
    headerPanel.setPreferredSize(new Dimension(App.WIDTH, HEADER_HEIGHT)); // Give the header a fixed height
    return headerPanel;
  }

  public static JPanel createHeader(String label) {
    return new HeaderFactory().createHeaderPanel(label);
  }
}
