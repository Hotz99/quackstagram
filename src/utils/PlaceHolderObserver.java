package utils;

import java.awt.Color;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

class PlaceholderObserver implements TextFieldObserver {

  private String placeholder;

  public PlaceholderObserver(String placeholder) {
    this.placeholder = placeholder;
  }

  @Override
  public void focusGained(FocusEvent e) {
    JTextField textField = (JTextField) e.getSource();
    if (textField.getText().equals(placeholder)) {
      textField.setText("");
      textField.setForeground(Color.BLACK);
    }
  }

  @Override
  public void focusLost(FocusEvent e) {
    JTextField textField = (JTextField) e.getSource();
    if (textField.getText().isEmpty()) {
      textField.setForeground(Color.GRAY);
      textField.setText(placeholder);
    }
  }

  @Override
  public void focusGained(JTextField textField, String placeholder) {
    // Not used
  }

  @Override
  public void focusLost(JTextField textField, String placeholder) {
    // Not used
  }
}
