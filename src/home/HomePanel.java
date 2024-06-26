package home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import app.App;
import database.models.Notification;
import database.models.Post;
import post.PostImageViewer;
import utils.BasePanel;
import utils.HeaderFactory;

public class HomePanel extends BasePanel {

	private static final int IMAGE_WIDTH = App.getAppWidth() - 100; // Width for the image posts
	private static final int IMAGE_HEIGHT = 150; // Height for the image posts
	private static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button

	private JPanel contentPanel;

	public HomePanel() {
		super(false, false, false);
		setLayout(new BorderLayout());

		add(HeaderFactory.createHeader("Quackstagram Home"), BorderLayout.NORTH);

		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		populateContentPanel();

		JScrollPane scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		add(scrollPane, BorderLayout.CENTER);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				populateContentPanel();
			}
		});
	}

	private JPanel createPostPanel() {
		JPanel postPanel = new JPanel();
		postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
		postPanel.setBackground(Color.WHITE);
		postPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		postPanel.setAlignmentX(CENTER_ALIGNMENT);
		return postPanel;
	}

	private void configureImageLabel(JLabel imageLabel, String fileName) {
		imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ImageIcon imageIcon = getImageIcon(fileName);
		if (imageIcon != null) {
			imageLabel.setIcon(imageIcon);
		} else {
			imageLabel.setText("Image not found");
		}
	}

	private JButton createLikeButton(
			int postId,
			JLabel likesLabel,
			JPanel postPanel) {
		JButton likeButton = new JButton("❤");
		likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		likeButton.setBackground(LIKE_BUTTON_COLOR);
		likeButton.setOpaque(true);
		likeButton.setBorderPainted(false);
		likeButton.addActionListener(e -> {
			Post post = postRepo.getByPostId(postId);

			if (likeRepo.toggleLike(postId, userManager.getCurrentUser().getUserId())) {

				likesLabel.setText(String.valueOf(post.getLikesCount() + 1));

				notificationRepo.saveNotification(
						new Notification(new Date(),
								post.getUserId(),
								userManager.getCurrentUser().getUsername() + " liked your post"));
			} else {
				likesLabel.setText(String.valueOf(Math.max(0, post.getLikesCount() - 1)));

			}

			postPanel.revalidate();
			postPanel.repaint();
		});
		return likeButton;
	}

	private void populateContentPanel() {
		contentPanel.removeAll();

		for (int followedUserId : followRepo.getFollowedByUserId(userManager.getCurrentUser().getUserId())) {

			Post latestPost = postRepo.getLatestPostByUserId(followedUserId);

			if (latestPost == null) {
				continue;
			}

			JPanel postPanel = createPostPanel();

			JLabel nameLabel = new JLabel(userRepo.getByUserId(latestPost.getUserId()).getUsername());
			nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			postPanel.add(nameLabel);

			JLabel imageLabel = new JLabel();
			configureImageLabel(imageLabel, latestPost.getImagePath());
			postPanel.add(imageLabel);

			JLabel captionLabel = new JLabel(latestPost.getCaption());
			captionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			postPanel.add(captionLabel);

			JLabel likesLabel = new JLabel(String.valueOf(latestPost.getLikesCount()));
			likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			postPanel.add(likesLabel);

			JButton likeButton = createLikeButton(
					latestPost.getPostId(),
					likesLabel,
					postPanel);
			postPanel.add(likeButton);

			contentPanel.add(postPanel);

			imageLabel.addMouseListener(
					new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							App.getPostViewer().displayImage(
									latestPost.getImagePath(),
									PostImageViewer.ImageType.HOME);

							System.out.println(likesLabel.getText());
						}
					});

			JPanel spacingPanel = new JPanel();
			spacingPanel.setPreferredSize(new Dimension(App.getAppWidth() - 10, 5));
			spacingPanel.setBackground(new Color(230, 230, 230));

			contentPanel.add(spacingPanel);
		}

		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private ImageIcon getImageIcon(String fileName) {
		try {
			BufferedImage originalImage = ImageIO.read(new File(appPaths.UPLOADED + fileName));
			Image scaledImage = originalImage.getScaledInstance(
					App.getAppWidth() - 40,
					App.getAppHeight() - 20,
					Image.SCALE_SMOOTH);
			return new ImageIcon(scaledImage);
		} catch (IOException ex) {
			System.out.println("could not find image: " + fileName);
			// ex.printStackTrace();
		}

		return null;
	}
}
