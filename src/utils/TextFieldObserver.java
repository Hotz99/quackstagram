package utils;

import java.awt.event.FocusListener;
import javax.swing.JTextField;

interface TextFieldObserver extends FocusListener {
  public void focusLost(JTextField textField, String placeholder);

  public void focusGained(JTextField textField, String placeholder);
}
