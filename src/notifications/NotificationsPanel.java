package notifications;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;

import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import app.App;
import auth.UserManager;
import database.models.Notification;
import database.repositories.NotificationRepository;
import database.repositories.UserRepository;
import utils.BasePanel;
import utils.HeaderFactory;

public class NotificationsPanel extends BasePanel {

  private final JPanel contentPanel = new JPanel();

  public NotificationsPanel() {
    super(false, false, false);
    JPanel headerPanel = HeaderFactory.createHeader(" Notifications 🐥");

    JScrollPane scrollPane = new JScrollPane(createContentPanel(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    add(headerPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        populateNotifications();
      }
    });
  }

  private JPanel createContentPanel() {
    this.contentPanel.setLayout(new BoxLayout(this.contentPanel, BoxLayout.Y_AXIS));

    populateNotifications();

    return this.contentPanel;
  }

  private void populateNotifications() {
    this.contentPanel.removeAll();
    System.out.println("Populating notifications");

    for (Notification notification : notificationRepo.getAllByUserId(userManager.getCurrentUser().getUserId())) {
      this.contentPanel.add(createNotificationEntry(notification));
    }

    refresh();
  }

  private JPanel createNotificationEntry(Notification notification) {
    JPanel labelPanel = new JPanel(new BorderLayout());
    JLabel notificationLabel = new JLabel(notification.getContent());
    notificationLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
    notificationLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    notificationLabel.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(java.awt.event.MouseEvent e) {
            notificationLabel.setBackground(Color.LIGHT_GRAY);
          }

          @Override
          public void mouseExited(java.awt.event.MouseEvent e) {
            notificationLabel.setBackground(null);
          }
        });
    JButton clearButton = new JButton(" X ");
    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        notificationRepo.deleteByNotificationId(notification.getNotificationId());
        populateNotifications();
      }
    });
    labelPanel.add(notificationLabel, BorderLayout.CENTER);
    labelPanel.add(clearButton, BorderLayout.EAST);
    labelPanel.setOpaque(true);
    return labelPanel;
  }
}
