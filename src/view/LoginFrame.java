package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;

public class LoginFrame extends JFrame {
	private static final String TITLE = "Grading System";
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

		JLabel labelTitle = new JLabel("Grading System");
		labelTitle.setBounds(SizeManager.titleBounds);
		labelTitle.setOpaque(true);
		labelTitle.setBackground(ColorManager.primaryColor);
		labelTitle.setForeground(ColorManager.lightColor);
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitle.setVerticalAlignment(SwingConstants.CENTER);
		labelTitle.setFont(FontManager.fontTitle);
		add(labelTitle);

		JButton buttonLogin = new JButton("Login");
		buttonLogin.setBounds(SizeManager.buttonLoginBounds);
		buttonLogin.setBackground(ColorManager.lightColor);
		buttonLogin.setForeground(ColorManager.primaryColor);
		buttonLogin.setFont(FontManager.fontLogin);
		buttonLogin.setFocusPainted(false);
		buttonLogin.addActionListener(e -> {
			new MainFrame();
			dispose();
		});
		add(buttonLogin);

		setVisible(true);
	}
}