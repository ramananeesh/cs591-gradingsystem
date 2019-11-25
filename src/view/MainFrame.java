package view;

import helper.SizeManager;

import javax.swing.*;

public class MainFrame extends JFrame {

	/** The background image file */
	private static final String BACKGROUND_PICTURE_FILE_NAME = "src/resource/background.jpg";

	/**
	 * Initializes a newly created {@code MainFrame} object
	 */
	public MainFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setContentPane(new JLabel(new ImageIcon(BACKGROUND_PICTURE_FILE_NAME)));
		setBounds(SizeManager.windowBounds);
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		add(new CoursePanel(this));
		setVisible(true);
	}

	/**
	 * Switch JPanel contained in this JFrame from one to another.
	 *
	 * @param from the JPanel that will be removed
	 * @param to   the JPanel that will be added
	 */
	public void switchPanel(JPanel from, JPanel to) {
		from.setVisible(false);
		remove(from);
		add(to);
		// revalidate(); // TODO if we use some kind of Layout instead of setLayout(null), we will need this line.
		repaint();
	}
}
