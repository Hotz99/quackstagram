package home;

import app.App;
import auth.UserManager;
import database.FollowRepository;
import database.LikeRepository;
import database.PostRepository;
import post.Post;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import utils.AppPathsSingleton;
import utils.BasePanel;
import utils.HeaderFactory;

public class HomePanel extends BasePanel {

	private static final int IMAGE_WIDTH = App.WIDTH - 100; // Width for the image posts
	private static final int IMAGE_HEIGHT = 150; // Height for the image posts
	private static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button

	private final AppPathsSingleton appPaths = AppPathsSingleton.getInstance();
	private final UserManager userManager = UserManager.getInstance();
	private final FollowRepository followRepo = FollowRepository.getInstance();
	private final PostRepository postRepo = PostRepository.getInstance();
	private final LikeRepository likeRepo = LikeRepository.getInstance();

	private CardLayout cardLayout;
	private JPanel contentPanel;
	private JPanel cardPanel;
	private JPanel imageViewPanel;

	public HomePanel() {
		super(false, false, false);
		add(HeaderFactory.createHeader("Quackstagram Home"), BorderLayout.NORTH);

		this.contentPanel = new JPanel();
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		populateContent();

		JScrollPane homePanel = new JScrollPane(contentPanel);
		homePanel.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		imageViewPanel = new JPanel(new BorderLayout());
		cardPanel = new JPanel(cardLayout = new CardLayout());
		cardPanel.add(homePanel, "Home");
		cardPanel.add(imageViewPanel, "ImageView");

		add(cardPanel, BorderLayout.CENTER);
	}

	private JPanel createItemPanel() {
		JPanel itemPanel = new JPanel();
		itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
		itemPanel.setBackground(Color.WHITE);
		itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		itemPanel.setAlignmentX(CENTER_ALIGNMENT);
		return itemPanel;
	}

	private void configureImageLabel(JLabel imageLabel, String imagePath) {
		imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ImageIcon imageIcon = getImageIcon(imagePath);
		if (imageIcon != null) {
			imageLabel.setIcon(imageIcon);
		} else {
			imageLabel.setText("Image not found");
		}
	}

	private JButton createLikeButton(
			int postId,
			JLabel likesLabel,
			JPanel itemPanel) {
		JButton likeButton = new JButton("❤");
		likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		likeButton.setBackground(LIKE_BUTTON_COLOR);
		likeButton.setOpaque(true);
		likeButton.setBorderPainted(false);
		likeButton.addActionListener(e -> {
			likeRepo.toggleLike(postId, userManager.getCurrentUser().getUserId());
			likesLabel.setText(String.valueOf(postRepo.getById(postId).getLikesCount()));
			itemPanel.revalidate();
			itemPanel.repaint();
		});
		return likeButton;
	}

	private void populateContent() {
		for (int followedUserId : followRepo.getFollowedByUserId(userManager.getCurrentUser().getUserId())) {
			Post latestPost = postRepo.getLatestPostByUserId(followedUserId);

			if (latestPost == null) {
				continue;
			}

			JPanel postPanel = createItemPanel();

			JLabel nameLabel = new JLabel(latestPost.getCaption());
			nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			postPanel.add(nameLabel);

			JLabel imageLabel = new JLabel();
			configureImageLabel(imageLabel, latestPost.getImagePath());
			postPanel.add(imageLabel);

			JLabel captionLabel = new JLabel(latestPost.getCaption());
			captionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			postPanel.add(captionLabel);

			JLabel likesLabel = new JLabel(String.valueOf(likeRepo.getLikesCountByPostId(latestPost.getPostId())));
			likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			postPanel.add(likesLabel);

			JButton likeButton = createLikeButton(
					latestPost.getPostId(),
					likesLabel,
					postPanel);
			postPanel.add(likeButton);

			this.contentPanel.add(postPanel);

			imageLabel.addMouseListener(
					new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							App.imageViewer.displayImage(" Quackstagram Home ", latestPost.getImagePath());
						}
					});

			addSpacingPanel(this.contentPanel);
		}
	}

	private void addSpacingPanel(JPanel panel) {
		JPanel spacingPanel = new JPanel();
		spacingPanel.setPreferredSize(new Dimension(App.WIDTH - 10, 5));
		spacingPanel.setBackground(new Color(230, 230, 230));
		panel.add(spacingPanel);
	}

	private ImageIcon getImageIcon(String fileName) {
		try {
			BufferedImage originalImage = ImageIO.read(new File(appPaths.UPLOADED + fileName));
			Image scaledImage = originalImage.getScaledInstance(
					App.WIDTH - 40,
					App.HEIGHT - 20,
					Image.SCALE_SMOOTH);
			return new ImageIcon(scaledImage);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
