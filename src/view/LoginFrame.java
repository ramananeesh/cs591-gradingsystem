package view;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import db.Read;
import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

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
		setBounds(SizeManager.getLoginWindowBounds());

		JLabel titleLabel = new JLabel("Grading System");
		titleLabel.setBounds(SizeManager.getTitleBounds());
		titleLabel.setOpaque(true);
		titleLabel.setBackground(ColorManager.getPrimaryColor());
		titleLabel.setForeground(ColorManager.getLightColor());
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(FontManager.getFontTitle());
		add(titleLabel);

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(SizeManager.getButtonLoginBounds());
		loginButton.setBackground(ColorManager.getLightColor());
		loginButton.setForeground(ColorManager.getPrimaryColor());
		loginButton.setFont(FontManager.getFontLogin());
		loginButton.setFocusPainted(false);

		UIManager.put("PasswordField.font", FontManager.getFontPassword());
		UIManager.put("Label.font", FontManager.getFontPassword());
		UIManager.put("Button.font", FontManager.getFontPassword());

		loginButton.addActionListener(e -> {
			JPasswordField passwordField = new JPasswordField();
			Object[] fields = {"Password: ", passwordField};
			UIManager.put("OptionPane.minimumSize",
					new Dimension(SizeManager.getOptionPaneWidth(), SizeManager.getOptionPaneRowHeight() * fields.length));

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

	private boolean checkPassword(String password) {
		HashMap<String, String> allUsers = getAllUsers();
		if (password == null) {
			return false;
		}
		return password.equals(allUsers.get("admin"));
	}

	private HashMap<String, String> getAllUsers() {
		return Read.getUsersForSystem();
	}

}