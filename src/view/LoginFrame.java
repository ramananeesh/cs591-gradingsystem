package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.*;

import db.Read;

/**
 * The {@code LoginFrame} class represents the frame for logging in the grading
 * system.
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

//		UIManager.put("Table.font", FontManager.fontMenuTable);
//		UIManager.put("TableHeader.font", FontManager.fontMenuTable);
//		UIManager.put("Menu.font", FontManager.fontMenu);
//		UIManager.put("MenuItem.font", FontManager.fontMenu);
//		UIManager.put("TextField.font", FontManager.fontLabel);
//		UIManager.put("ComboBox.font", FontManager.fontLabel);

		UIManager.put("PasswordField.font", new Font("Cascadia", Font.BOLD, 35));
		UIManager.put("Label.font", new Font("Cascadia", Font.BOLD, 35));
		UIManager.put("Button.font", new Font("Cascadia", Font.BOLD, 35));

		loginButton.addActionListener(e -> {
			JPasswordField passwordField = new JPasswordField();
			Object[] fields = { "Password: ", passwordField };
			UIManager.put("OptionPane.minimumSize",
					new Dimension(SizeManager.optionPaneWidth, SizeManager.optionPaneRowHeight * fields.length));

			int r = JOptionPane.showConfirmDialog(null, fields, "Enter Password: ", JOptionPane.OK_CANCEL_OPTION);

			if (r == JOptionPane.OK_OPTION) {
				String reply = passwordField.getText();
				if (reply != null && !checkPassword(reply)) {
					while (true) {
						
						int repl = JOptionPane.showConfirmDialog(null, fields, "Enter Password: ", JOptionPane.OK_CANCEL_OPTION);
						if (repl == JOptionPane.CANCEL_OPTION)
							break;
						if (checkPassword(passwordField.getText())) {
							new MainFrame();
							dispose();
							break;
						}

					}
				} else {
					if (reply != null) {
						new MainFrame();
						dispose();
					}
				}

			}

		});
		add(loginButton);

		setVisible(true);
	}

	public boolean checkPassword(String password) {
		HashMap<String, String> allUsers = getAllUsers();
		if (password == null) {
			return false;
		}
		
		if (password.equals(allUsers.get("admin")))
			return true;

		return false;
	}

	public HashMap<String, String> getAllUsers() {
		HashMap<String, String> map = Read.getUsersForSystem();

		return map;
	}
}