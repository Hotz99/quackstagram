package notifications;

import java.awt.*;
import javax.swing.*;
import utils.BasePanel;
import utils.HeaderFactory;

public class NotificationsPanel extends BasePanel {

  public NotificationsPanel() {
    super(false, false, false);
    JPanel headerPanel = HeaderFactory.createHeader(" Notifications 🐥");

    JScrollPane scrollPane = new JScrollPane(createContentPanel());
    scrollPane.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    add(headerPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
  }

  private JPanel createContentPanel() {
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    contentPanel.add(new JLabel("You aint got no notifications"));

    return contentPanel;
  }
}
