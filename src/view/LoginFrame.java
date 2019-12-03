package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;

/**
 * The {@code LoginFrame} class represents the frame for logging in the grading system.
 */
public class LoginFrame extends JFrame {
	/** The title for the window when LoginFrame displays */
	private static final String TITLE = "Grading System";

	/** The background image file */
	private static final String BACKGROUND_PICTURE_FILE_NAME = "src/resource/background.jpg";

	/**
	 * Initializes a newly created {@code LoginFrame} object
	 */
	public LoginFrame() {
		setTitle(TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setContentPane(new JLabel(new ImageIcon(BACKGROUND_PICTURE_FILE_NAME)));
		setBounds(SizeManager.loginWindowBounds);

		JLabel titleLabel = new JLabel("Grading System");
		titleLabel.setBounds(SizeManager.titleBounds);
		titleLabel.setOpaque(true);
		titleLabel.setBackground(ColorManager.primaryColor);
		titleLabel.setForeground(ColorManager.lightColor);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(FontManager.fontTitle);
		add(titleLabel);

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(SizeManager.buttonLoginBounds);
		loginButton.setBackground(ColorManager.lightColor);
		loginButton.setForeground(ColorManager.primaryColor);
		loginButton.setFont(FontManager.fontLogin);
		loginButton.setFocusPainted(false);
		loginButton.addActionListener(e -> {
			new MainFrame();
			dispose();
		});
		add(loginButton);

		setVisible(true);
	}
}